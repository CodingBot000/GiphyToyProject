package com.exam.sample.model.repository.detail

import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.datasource.local.favorite.FavoriteInfoLocalDataSource
import com.exam.sample.model.datasource.remote.detail.DetailDataRemoteDataSource
import io.reactivex.Single
import javax.inject.Inject

class DetailDataRepositoryImpl @Inject constructor(
    private val detailDataRemoteDataSource: DetailDataRemoteDataSource,
    private val favoriteInfoLocalDataSource: FavoriteInfoLocalDataSource
) : DetailDataRepository {
    override fun requestDetailData(id: String): Single<TrendingData> {
        return detailDataRemoteDataSource.requestDetailData(id)
    }

    override fun getFavoriteDB(userId: String): Single<FavoriteInfo> {
        return favoriteInfoLocalDataSource.getInfo(userId)
    }

    override fun insertFavoriteDB(favoriteInfo: FavoriteInfo): Single<Long> {
        return favoriteInfoLocalDataSource.insertInfo(favoriteInfo)
    }

    override fun removeFavoriteDB(favoriteInfo: FavoriteInfo): Single<Int> {
        return favoriteInfoLocalDataSource.removeInfo(favoriteInfo)
    }

    override fun updateFavoriteDB(favoriteInfo: FavoriteInfo): Single<Int> {
        return favoriteInfoLocalDataSource.updateInfo(favoriteInfo)
    }
}
