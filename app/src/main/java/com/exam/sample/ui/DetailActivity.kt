package com.exam.sample.ui


import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.EmptyResultSetException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.exam.sample.R
import com.exam.sample.adapter.StaggeredAdapter
import com.exam.sample.common.clear
import com.exam.sample.common.setImage
import com.exam.sample.databinding.ActivityDetailBinding
import com.exam.sample.livedata.EventObserver
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.data.InteractionData
import com.exam.sample.ui.base.BaseActivity
import com.exam.sample.utils.Const
import com.exam.sample.utils.Status
import com.exam.sample.utils.extention.glideLoadForUrl
import com.exam.sample.utils.extention.slideDown
import com.exam.sample.utils.extention.slideOutToRight
import com.exam.sample.utils.extention.touchEffect
import com.exam.sample.utils.shareUrl
import com.exam.sample.utils.toastMsg
import com.exam.sample.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>() {
    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.activity_detail

    private val viewModel by viewModels<DetailViewModel>()
    private val adapterRecycler: StaggeredAdapter by lazy {
        StaggeredAdapter(itemListClick = { item, gridBindingItem ->
            gridBindingItem.img.touchEffect(this@DetailActivity)
            //같은 디테일이면 현재 상태에서 데이터만 교체한다.
            interactionDataTmp = item.convertInteractionData()
            replaceMainImage()
        })
    }
    private lateinit var lManager: StaggeredGridLayoutManager

    private lateinit var interactionDataTmp: InteractionData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initData()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getIntentData() {
        interactionDataTmp = intent.getParcelableExtra<InteractionData>(Const.EXTRA_KEY_INTERACTION)!!
        viewModel.interactionData = interactionDataTmp.copy()
        setImage(binding.detailImage, viewModel.interactionData?.urlDownSized)

    }

    override fun init() {
        getIntentData()
        if (!adapterRecycler.hasObservers())
            adapterRecycler.setHasStableIds(true)
        lManager =
            StaggeredGridLayoutManager(Const.LIST_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        binding.toolbar.navigationIcon
        binding.recyclerView.apply {
            layoutManager = lManager
            adapter = adapterRecycler
        }

        onClickListener()
    }

    override fun initObserver() {
        viewModel.apply {
            isLoading.observe(
                this@DetailActivity,
                EventObserver {
                    binding.progress.isVisible = it
                }
            )

            dbEvent.observe(
                this@DetailActivity,
                EventObserver {
                    when (it.status) {
                        Status.SUCCESS -> {
                            if (it.data?.flag == Const.DB_SELECT) {
                                binding.llTopInfoView.checkBoxFavorite.isChecked = it.data.data != null
                            } else {
                                if (it.data?.flag == Const.DB_INSERT)
                                    makeSnackBar(R.string.favorite_check, R.string.favorite_uncheck)
                                else
                                    makeSnackBar(R.string.favorite_uncheck, R.string.favorite_check)
                            }
                        }

                        Status.ERROR -> {
                            if (it.data?.data is EmptyResultSetException) {
                                Log.v(Const.LOG_TAG, "Query returned Empty")
                                binding.llTopInfoView.checkBoxFavorite.isChecked = false
                            } else {
                                toastMsg(it.message ?: "")
                            }
                        }
                    }
                }
            )

            itemLiveData.observe(
                this@DetailActivity,
                EventObserver {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { data -> adapterRecycler.initItem(data.trendingItems) }
                        }

                        Status.ERROR -> {
                            toastMsg(it.message ?: "")
                        }
                    }
                }
            )

            favoriteCheckEvent.observe(
                this@DetailActivity,
                EventObserver {
                    interactionDataTmp.apply {
                        if (it) {
                            insertFavorite(
                                FavoriteInfo(
                                    userId,
                                    urlSmall,
                                    type
                                )
                            )
                        } else {
                            removeFavorite(
                                FavoriteInfo(
                                    userId,
                                    urlSmall,
                                    type
                                )
                            )
                        }
                    }
                }
            )

            btnSimpleEvent.observe(
                this@DetailActivity,
                EventObserver {
                    when (it) {
                        Const.BTN_EVENT_SEND -> {
                            shareUrl(interactionDataTmp.urlEmbeded)
                        }
                        Const.BTN_EVENT_SHARE -> {
                            toastMsg(R.string.msgMore)
                        }

                    }
                }
            )
        }
    }
    private fun initData() {
        viewModel.getFavorite(interactionDataTmp.userId)
        viewModel.getDetailData(interactionDataTmp.userId)
    }

    private fun onClickListener() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun replaceMainImage() {
        binding.detailImage.clear()
        binding.appBarLayout.setExpanded(true, true)
        viewModel.getFavorite(interactionDataTmp.userId)

        viewModel.showProgressForView()

        binding.detailImage.glideLoadForUrl(
            interactionDataTmp.urlDownSized,
            onLoadFailed = {
                viewModel.hideProgressForView()
            }, onResourceReady = {
                viewModel.hideProgressForView()
            }
        )
    }

    private fun makeSnackBar(msgResId: Int, btnNameResId: Int) {
        val snackbar = Snackbar.make(
            binding.recyclerView,
            msgResId,
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction(getString(btnNameResId)) {
            binding.llTopInfoView.checkBoxFavorite.performClick()
            snackbar.dismiss()
        }
        snackbar.show()
    }

    override fun finish() {
        setResult(RESULT_OK)
        slideDown()
        super.finish()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}
