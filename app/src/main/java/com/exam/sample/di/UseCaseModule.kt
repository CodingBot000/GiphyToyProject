package com.exam.sample.di

import com.exam.sample.api.ApiService
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
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideUseCaseDbInsert(favoriteInfoRepository: FavoriteInfoRepository) =
        UseCaseDbInsert(favoriteInfoRepository)

    @Singleton
    @Provides
    fun provideUseCaseDbRemove(favoriteInfoRepository: FavoriteInfoRepository) =
        UseCaseDbRemove(favoriteInfoRepository)

    @Singleton
    @Provides
    fun provideUseCaseDbRemoveAll(favoriteInfoRepository: FavoriteInfoRepository) =
        UseCaseDbRemoveAll(favoriteInfoRepository)


    @Singleton
    @Provides
    fun provideUseCaseDbSelect(favoriteInfoRepository: FavoriteInfoRepository) =
        UseCaseDbSelect(favoriteInfoRepository)

    @Singleton
    @Provides
    fun provideUseCaseDbSelectAll(favoriteInfoRepository: FavoriteInfoRepository) =
        UseCaseDbSelectAll(favoriteInfoRepository)

}