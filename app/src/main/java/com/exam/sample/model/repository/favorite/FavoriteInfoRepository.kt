package com.exam.sample.model.repository.favorite

import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.data.TrendingData
import io.reactivex.Single

interface FavoriteInfoRepository {
    fun requestGIFsByIds(idsList: String): Single<TrendingData>
    fun getFavoriteAllDB(): Single<List<FavoriteInfo>>
    fun getFavoriteDB(userId: String): Single<FavoriteInfo>
    fun insertFavoriteDB(favoriteInfo: FavoriteInfo): Single<Long>
    fun removeFavoriteDB(favoriteInfo: FavoriteInfo): Single<Int>
    fun removeAllFavoriteDB(): Single<Int>
    fun updateFavoriteDB(favoriteInfo: FavoriteInfo): Single<Int>
}
