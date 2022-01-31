package com.exam.sample.model.repository.trending

import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.datasource.remote.trending.TrendingRemoteDataSource
import io.reactivex.Single
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val trendingRemoteDataSource: TrendingRemoteDataSource
) : TrendingRepository {
    override fun requestTrendingData(offset: Int, rating: String): Single<TrendingData> {
        return trendingRemoteDataSource.requestTrendingData(offset, rating)
    }
}
