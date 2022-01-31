package com.exam.sample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exam.sample.R
import com.exam.sample.databinding.GridItemBinding
import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.utils.Const
import com.exam.sample.utils.extention.setCellSize
import com.exam.sample.utils.extention.setRainBowBackgroundColorByPosition
import java.util.ArrayList

class StaggeredAdapter(private val itemListClick: (TrendingDetail, GridItemBinding) -> Unit) :
    RecyclerView.Adapter<StaggeredAdapter.ItemViewHolder>() {

    private var recyclerItemList: ArrayList<TrendingDetail> = ArrayList()

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
        recyclerItemList[position].let {
            holderItem.bind(it)
        }
    }

    override fun getItemId(position: Int): Long {
        return (recyclerItemList[position]).id.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return recyclerItemList.size
    }

    fun initItem(list: ArrayList<TrendingDetail>) {
        recyclerItemList.clear()
        recyclerItemList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearItem() {
        recyclerItemList.clear()
        notifyDataSetChanged()
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
            binding.img.setRainBowBackgroundColorByPosition(position)

            binding.executePendingBindings()
        }
    }
}
