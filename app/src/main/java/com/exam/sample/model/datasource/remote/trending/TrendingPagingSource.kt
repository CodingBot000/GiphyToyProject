package com.exam.sample.model.datasource.remote.trending

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.exam.sample.api.ApiService
import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.utils.Const
import javax.inject.Inject


class TrendingPagingSource @Inject constructor(private val apiService: ApiService)
    : PagingSource<Int, TrendingDetail>() {

    private lateinit var rating: String

    fun setData(rating: String) {
        this.rating = rating
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrendingDetail> {
        val curPage = params.key ?: 1
        var nextKey: Int? = curPage + 1

        return try {
            val response = apiService.getTrendingDataRequestFlow(Const.API_KEY, Const.LIMIT, curPage * Const.LIMIT, rating)
            if (response.pagination.total_count < curPage * Const.LIMIT) {
                nextKey = null
            }

            LoadResult.Page(
                data = response.trendingItems,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, TrendingDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}