package com.exam.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.exam.sample.R
import com.exam.sample.adapter.ReposLoadStateAdapter
import com.exam.sample.adapter.StaggeredPagingAdapter
import com.exam.sample.databinding.FragmentTrendingBinding
import com.exam.sample.livedata.EventObserver
import com.exam.sample.ui.DetailActivity
import com.exam.sample.ui.base.BaseFragment
import com.exam.sample.utils.*
import com.exam.sample.utils.extention.startActivityDetailExtras
import com.exam.sample.utils.extention.touchEffect
import com.exam.sample.viewmodel.MainSharedViewModel
import com.exam.sample.viewmodel.TrendingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendingFragment : BaseFragment<FragmentTrendingBinding, TrendingViewModel>(), SwipeRefreshLayout.OnRefreshListener {
    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.fragment_trending

    private val sharedViewModel by activityViewModels<MainSharedViewModel>()
    private val viewModel by viewModels<TrendingViewModel>()
    private val adapterRecycler: StaggeredPagingAdapter by lazy {
        StaggeredPagingAdapter(
            itemListClick = { item, gridBindingItem ->
                sharedViewModel.notiItemTabEvent()
                gridBindingItem.img.touchEffect(requireActivity())
                requireActivity().startActivityDetailExtras(DetailActivity::class.java, item)
        })
    }

    private lateinit var lManager: StaggeredGridLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initData()
        return binding.root
    }

    override fun init() {
        binding.apply {
            swipeLayout.setOnRefreshListener(this@TrendingFragment)
            lManager =
                StaggeredGridLayoutManager(Const.LIST_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)

            recyclerView.apply {
                layoutManager = lManager
                itemAnimator = null
                adapter = adapterRecycler

                adapter = adapterRecycler.withLoadStateHeaderAndFooter(
                    header = ReposLoadStateAdapter { adapterRecycler.retry() },
                    footer = ReposLoadStateAdapter { adapterRecycler.retry() }
                )
            }
            adapterRecycler.addLoadStateListener { loadState ->
                // 로딩바 표시가 너무 빨리로드되서 안보여서 별도 표기 해보았습니다.
                progress.isVisible = loadState.append is LoadState.Loading
                swipeLayout.isRefreshing = loadState.source.refresh !is LoadState.NotLoading
            }
        }
    }

    override fun onRefresh() {
        adapterRecycler.clearItem()
        binding.root.delayOnLifecycle(500L) {
            viewModel.getTrendingData()
            binding.swipeLayout.isRefreshing = false
        }
    }

    override fun initObserver() {
        viewModel.apply {
            isLoading.observe(
                requireActivity(),
                EventObserver {
                    binding.progress.isVisible = it
                }
            )

            dataListLiveData.observe(viewLifecycleOwner, Observer {
                it?.let {  adapterRecycler.submitData(lifecycle, it) }
            })
        }
    }

    private fun initData() {
        viewModel.apply {
            getTrendingData()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
