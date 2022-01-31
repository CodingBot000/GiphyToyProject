package com.exam.sample.adapter



import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.exam.sample.R
import com.exam.sample.databinding.GridItemBinding

import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.utils.Const
import com.exam.sample.utils.extention.imageViewBorder
import com.exam.sample.utils.extention.setCellSize
import com.exam.sample.utils.extention.setRainBowBackgroundColorByPosition
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*
import kotlin.coroutines.suspendCoroutine


class StaggeredPagingAdapter (
    private val itemListClick: (TrendingDetail, GridItemBinding) -> Unit
) : PagingDataAdapter<TrendingDetail, StaggeredPagingAdapter.ItemViewHolder>(REPO_COMPARATOR)  {

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("clearItem exception :${exception.message}")
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemViewHolder {
        var binding: GridItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.grid_item,
            parent,
            false
        )

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holderItem: ItemViewHolder, position: Int) {
        getItem(position).let {
            holderItem.bind(it!!)
        }
    }

    fun clearItem() {
        val coroutineContext = Dispatchers.Main + exceptionHandler
        CoroutineScope(coroutineContext).launch {
            submitData(PagingData.empty())
            joinAll()
        }
    }

    inner class ItemViewHolder(private val binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TrendingDetail) {
            binding.item = item
            binding.root.setOnClickListener {
                itemListClick(item, binding)
            }

            val width = item.images.fixed_width_small.width?.toFloat()
            val height = item.images.fixed_width_small.height?.toFloat()
            binding.img.layoutParams = binding.img.setCellSize(
                width ?: Const.SCREEN_WIDTH_HALF,
                height ?: Const.SCREEN_WIDTH_HALF
            )
            binding.img.setRainBowBackgroundColorByPosition(absoluteAdapterPosition)

            binding.executePendingBindings()
        }
    }


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<TrendingDetail>() {
            override fun areItemsTheSame(oldItem: TrendingDetail, newItem: TrendingDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TrendingDetail, newItem: TrendingDetail): Boolean {
                return oldItem == newItem
            }
        }
    }
}
