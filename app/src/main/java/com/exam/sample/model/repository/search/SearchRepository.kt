package com.exam.sample.model.repository.search

import com.exam.sample.model.data.TrendingData
import io.reactivex.Single

interface SearchRepository {
    fun requestSearchData(keyword: String, offset: Int): Single<TrendingData>
}
