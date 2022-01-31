package com.exam.sample.model.repository.search

import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.datasource.remote.search.SearchRemoteDataSource
import io.reactivex.Single
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchRepository {
    override fun requestSearchData(keyword: String, offset: Int): Single<TrendingData> {
        return searchRemoteDataSource.requestSearchData(keyword, offset)
    }
}
