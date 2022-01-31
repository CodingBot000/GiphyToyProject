package com.exam.sample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.utils.Const

@Database(entities = [FavoriteInfo::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java,  Const.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

}