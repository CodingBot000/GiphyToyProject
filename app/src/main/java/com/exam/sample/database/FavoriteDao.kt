package com.exam.sample.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.exam.sample.model.data.FavoriteInfo
import io.reactivex.Single

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM TABLE_FAVORITE")
    fun getAll(): Single<List<FavoriteInfo>>

    @Query("SELECT * FROM TABLE_FAVORITE where userId = :userId")
    fun getData(userId: String): Single<FavoriteInfo>

    @Update
    fun update(favoriteInfo: FavoriteInfo): Single<Int>

    @Update
    fun update(list: List<FavoriteInfo>): Single<Int>

    @Delete
    fun delete(favoriteInfo: FavoriteInfo): Single<Int>

    @Delete
    fun delete(list: List<FavoriteInfo>): Single<Int>

    @Query("DELETE FROM TABLE_FAVORITE")
    fun deleteAll(): Single<Int>

    @Insert(onConflict = REPLACE)
    fun insert(favoriteInfo: FavoriteInfo): Single<Long>
}
