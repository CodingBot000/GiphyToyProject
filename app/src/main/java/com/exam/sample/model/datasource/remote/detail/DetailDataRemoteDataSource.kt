package com.exam.sample.model.datasource.remote.detail

import com.exam.sample.model.data.TrendingData
import io.reactivex.Single

interface DetailDataRemoteDataSource {
    fun requestDetailData(id: String): Single<TrendingData>
}
