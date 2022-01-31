package com.exam.sample.model.datasource.remote.search

import com.exam.sample.model.data.TrendingData
import io.reactivex.Single

interface SearchRemoteDataSource {
    fun requestSearchData(keyword: String, offset: Int): Single<TrendingData>
}
