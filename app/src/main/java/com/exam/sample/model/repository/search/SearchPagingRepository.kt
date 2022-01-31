package com.exam.sample.model.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.model.datasource.remote.search.SearchPagingSource
import com.exam.sample.model.datasource.remote.search.SearchRemoteDataSource
import com.exam.sample.utils.Const

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPagingRepository @Inject constructor(private val searchPagingSource: SearchPagingSource) {

    fun getPagingData(keyword: String) : Flow<PagingData<TrendingDetail>> {
        return Pager(
            PagingConfig(
                pageSize = Const.LIMIT,
            ),
            pagingSourceFactory = { searchPagingSource.apply { setData(keyword) }.also { it } }
        ).flow
    }

}