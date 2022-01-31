package com.exam.sample.model.datasource.remote.trending

import com.exam.sample.model.data.TrendingData
import io.reactivex.Single

interface TrendingRemoteDataSource {
    fun requestTrendingData(offset: Int, rating: String): Single<TrendingData>
}
