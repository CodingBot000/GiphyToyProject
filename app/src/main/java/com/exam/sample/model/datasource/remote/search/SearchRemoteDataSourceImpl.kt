package com.exam.sample.model.datasource.remote.search

import com.exam.sample.api.ApiService
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Const
import io.reactivex.Single
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    SearchRemoteDataSource {

    override fun requestSearchData(keyword: String, offset: Int): Single<TrendingData> {
        return apiService.getSearchFromKeyword(Const.API_KEY, Const.LIMIT, offset, keyword)
    }
}
