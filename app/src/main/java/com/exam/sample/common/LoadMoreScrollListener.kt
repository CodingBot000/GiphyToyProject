package com.exam.sample.common

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.exam.sample.utils.Const

class LoadMoreScrollListener(
    private val layoutManager: StaggeredGridLayoutManager,
    private val scrollLoadMoreListener: ScrollLoadMoreListener?
) : OnScrollListener() {
    private var totalCnt = 0
    private var offset = Const.OFFSET_DEFAULT
    private var isAddReady = false
    private var isFirstLoad = true
    private var visibleThreshold = Const.LIMIT
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    fun setParameters(totalItems: Int, isAddLoadReady: Boolean) {
        totalCnt = totalItems
        isAddReady = isAddLoadReady
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        scrollLoadMoreListener?.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        totalItemCount = layoutManager.itemCount
        lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPositions(null)[1]

        if (!isAddReady && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (isFirstLoad) {
                isFirstLoad = false
                return
            }

            if (offset >= totalCnt)
                return

            offset += Const.LIMIT
            if (offset > totalCnt)
                offset = totalCnt

            scrollLoadMoreListener?.onLoadMore(offset, isAddReady)
            isAddReady = true
        }
    }

    interface ScrollLoadMoreListener {
        fun onLoadMore(page: Int, isAddReady: Boolean)
        fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
    }
}
