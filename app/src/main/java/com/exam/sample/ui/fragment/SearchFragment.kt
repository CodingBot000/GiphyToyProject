package com.exam.sample.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.exam.sample.R
import com.exam.sample.adapter.ReposLoadStateAdapter
import com.exam.sample.adapter.StaggeredPagingAdapter
import com.exam.sample.databinding.FragmentSearchBinding
import com.exam.sample.livedata.EventObserver
import com.exam.sample.ui.DetailActivity
import com.exam.sample.ui.base.BaseFragment
import com.exam.sample.utils.*
import com.exam.sample.utils.extention.hideKeyboard
import com.exam.sample.utils.extention.startActivityDetailExtras
import com.exam.sample.utils.extention.touchEffect
import com.exam.sample.viewmodel.MainSharedViewModel
import com.exam.sample.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class SearchFragment constructor(private val closeEvent: () -> Unit) : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.fragment_search

    private val sharedViewModel by activityViewModels<MainSharedViewModel>()
    private val viewModel by viewModels<SearchViewModel>()
    private val adapterRecycler: StaggeredPagingAdapter by lazy {
        StaggeredPagingAdapter(
            itemListClick = { item, gridBindingItem ->
                sharedViewModel.notiItemTabEvent()
                gridBindingItem.img.touchEffect(requireActivity())
                requireActivity().startActivityDetailExtras(DetailActivity::class.java, item)
            }
        )
    }

    private lateinit var lManager: StaggeredGridLayoutManager
    private var keyword = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.viewModel = viewModel

        initData()

        return binding.root
    }

    override fun init() {
        lManager =
            StaggeredGridLayoutManager(Const.LIST_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)


        binding.apply {
//            swipeLayout.setOnRefreshListener(this@SearchFragment)

            recyclerView.apply {
                layoutManager = lManager
                itemAnimator = null
                adapter = adapterRecycler
                adapter = adapterRecycler.withLoadStateHeaderAndFooter(
                    header = ReposLoadStateAdapter { adapterRecycler.retry() },
                    footer = ReposLoadStateAdapter { adapterRecycler.retry() }
                )
                adapterRecycler.addLoadStateListener { loadState ->
                    // 로딩바 표시가 너무 빨리로드되서 안보여서 별도 표기 해보았습니다.
                    progress.isVisible = loadState.append is LoadState.Loading
//                    swipeLayout.isRefreshing = loadState.source.refresh !is LoadState.NotLoading
                }
                addOnScrollListener(object :RecyclerView.OnScrollListener()  {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        hideKeyboard()
                    }

                })
            }
        }
        initEditTextEvent()
        onClickListener()
    }

//    override fun onRefresh() {
//        adapterRecycler.clearItem()
//        binding.root.delayOnLifecycle(500L) {
//            viewModel.getSearchData(keyword)
////            binding.swipeLayout.isRefreshing = false
//        }
//
//    }

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

    }

    private fun onClickListener() {
        binding.apply {
            root.setOnTouchListener { _, _ -> true }
            searchToolbar.setNavigationOnClickListener {
                closeEvent()
            }
            fabDown.setOnClickListener {
                closeEvent()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initEditTextEvent() {

        binding.edtSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchKeyword = binding.edtSearch.text.toString()
                    if (searchKeyword.isEmpty())
                    {
                        toastMsg(R.string.edttext_hint)
                        return true
                    }
                    viewModel.getSearchData(searchKeyword)
                    binding.edtSearch.hideKeyboard()
                    return true
                }
                return false
            }
        })

        viewModel.getSearchKeyChangeObserver()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { enterKeyword ->
                if (enterKeyword == keyword)
                    return@subscribe
                keyword = enterKeyword
                if ( keyword.isEmpty()) {
                    adapterRecycler.clearItem()
                } else {
                    viewModel.getSearchData(keyword)
                }
            }

        binding.edtSearch.requestFocus()

        // Databinding을 사용하지않을 경우 아래 이용
//        val searchKetTW = SearchKeyTextWatcher()
//        searchKetTW.getSearchKeyChangeObserver()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                if (it == keyword)
//                    return@subscribe
//                keyword = it
//                if (it.isEmpty()) {
//                    adapterRecycler.clearItem()
//                } else {
//                    offset = 0
//                    viewModel.getSearch(keyword, offset)
//                }
//            }
//        edtSearch.addTextChangedListener(searchKetTW)
//        edtSearch.requestFocus()
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
