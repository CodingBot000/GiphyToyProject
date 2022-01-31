package com.exam.sample.model.datasource.local.favorite

import com.exam.sample.database.FavoriteDao
import com.exam.sample.model.data.FavoriteInfo
import io.reactivex.Single
import javax.inject.Inject

class FavoriteInfoLocalDataSourceImpl @Inject constructor(private val favoriteDao: FavoriteDao) :
    FavoriteInfoLocalDataSource {

    override fun getInfo(userId: String): Single<FavoriteInfo> {
        return favoriteDao.getData(userId)
    }

    override fun getInfoAll(): Single<List<FavoriteInfo>> {
        return favoriteDao.getAll()
    }

    override fun insertInfo(favoriteInfo: FavoriteInfo): Single<Long> {
        return favoriteDao.insert(favoriteInfo)
    }

    override fun removeInfo(favoriteInfo: FavoriteInfo): Single<Int> {
        return favoriteDao.delete(favoriteInfo)
    }

    override fun removeAllInfo(): Single<Int> {
        return favoriteDao.deleteAll()
    }

    override fun updateInfo(favoriteInfo: FavoriteInfo): Single<Int> {
        return favoriteDao.update(favoriteInfo)
    }
}
