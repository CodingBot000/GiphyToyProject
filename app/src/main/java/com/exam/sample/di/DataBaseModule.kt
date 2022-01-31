package com.exam.sample.di

import android.content.Context
import com.exam.sample.api.ApiService
import com.exam.sample.database.AppDatabase
import com.exam.sample.domain.usecase.*
import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.datasource.local.favorite.FavoriteInfoLocalDataSource
import com.exam.sample.model.datasource.remote.detail.DetailDataRemoteDataSource
import com.exam.sample.model.datasource.remote.detail.DetailDataRemoteDataSourceImpl
import com.exam.sample.model.datasource.remote.favorite.FavoriteInfoRemoteDataSource
import com.exam.sample.model.datasource.remote.favorite.FavoriteInfoRemoteDataSourceImpl
import com.exam.sample.model.datasource.remote.search.SearchRemoteDataSource
import com.exam.sample.model.datasource.remote.search.SearchRemoteDataSourceImpl
import com.exam.sample.model.datasource.remote.trending.TrendingRemoteDataSource
import com.exam.sample.model.datasource.remote.trending.TrendingRemoteDataSourceImpl
import com.exam.sample.model.repository.detail.DetailDataRepository
import com.exam.sample.model.repository.detail.DetailDataRepositoryImpl
import com.exam.sample.model.repository.favorite.FavoriteInfoRepository
import com.exam.sample.model.repository.favorite.FavoriteInfoRepositoryImpl
import com.exam.sample.model.repository.search.SearchRepository
import com.exam.sample.model.repository.search.SearchRepositoryImpl
import com.exam.sample.model.repository.trending.TrendingRepository
import com.exam.sample.model.repository.trending.TrendingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideFavoriteInfoDao(db: AppDatabase) = db.favoriteDao()
}