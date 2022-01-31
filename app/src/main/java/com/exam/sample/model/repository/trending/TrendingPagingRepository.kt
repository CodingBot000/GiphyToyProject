package com.exam.sample.model.repository.trending

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.model.datasource.remote.trending.TrendingPagingSource
import com.exam.sample.utils.Const

import kotlinx.coroutines.flow.Flow

class TrendingPagingRepository(private val trendingPagingSource: TrendingPagingSource) {

    fun getPagingData(rating: String = "") : Flow<PagingData<TrendingDetail>> {
        return Pager(
            PagingConfig(
                pageSize = Const.LIMIT,
            ),
            pagingSourceFactory = { trendingPagingSource.apply { setData(rating) }.also { it } }
        ).flow
    }
}