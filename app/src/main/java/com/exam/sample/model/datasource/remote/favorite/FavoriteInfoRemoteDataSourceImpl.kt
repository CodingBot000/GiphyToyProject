package com.exam.sample.model.datasource.remote.favorite

import com.exam.sample.api.ApiService
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Const
import io.reactivex.Single
import javax.inject.Inject

class FavoriteInfoRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    FavoriteInfoRemoteDataSource {

    override fun requestGIFsByIds(idsList: String): Single<TrendingData> {
        return apiService.getGIFsByIDs(Const.API_KEY, idsList)
    }
}
