package com.exam.sample.di

import com.exam.sample.api.ApiService
import com.exam.sample.database.FavoriteDao
import com.exam.sample.domain.usecase.UseCaseGetTrendingData
import com.exam.sample.model.datasource.local.favorite.FavoriteInfoLocalDataSource
import com.exam.sample.model.datasource.local.favorite.FavoriteInfoLocalDataSourceImpl
import com.exam.sample.model.datasource.remote.detail.DetailDataRemoteDataSource
import com.exam.sample.model.datasource.remote.detail.DetailDataRemoteDataSourceImpl
import com.exam.sample.model.datasource.remote.favorite.FavoriteInfoRemoteDataSource
import com.exam.sample.model.datasource.remote.favorite.FavoriteInfoRemoteDataSourceImpl
import com.exam.sample.model.datasource.remote.search.SearchPagingSource
import com.exam.sample.model.datasource.remote.search.SearchRemoteDataSource
import com.exam.sample.model.datasource.remote.search.SearchRemoteDataSourceImpl
import com.exam.sample.model.datasource.remote.trending.TrendingPagingSource
import com.exam.sample.model.datasource.remote.trending.TrendingRemoteDataSource
import com.exam.sample.model.datasource.remote.trending.TrendingRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideTrendingRemoteDataSource(
        apiService: ApiService
    ): TrendingRemoteDataSource = TrendingRemoteDataSourceImpl(apiService)


    @Singleton
    @Provides
    fun provideSearchRemoteDataSource(
        apiService: ApiService
    ): SearchRemoteDataSource = SearchRemoteDataSourceImpl(apiService)

    @Singleton
    @Provides
    fun provideDetailDataRemoteDataSource(
        apiService: ApiService
    ): DetailDataRemoteDataSource = DetailDataRemoteDataSourceImpl(apiService)

    @Singleton
    @Provides
    fun provideFavoriteInfoRemoteDataSource(
        apiService: ApiService
    ): FavoriteInfoRemoteDataSource = FavoriteInfoRemoteDataSourceImpl(apiService)

    @Singleton
    @Provides
    fun provideFavoriteInfoLocalDataSource(favoriteDao: FavoriteDao):
            FavoriteInfoLocalDataSource = FavoriteInfoLocalDataSourceImpl(favoriteDao)




    @Singleton
    @Provides
    fun provideTrendingPagingSource(apiService: ApiService):
            TrendingPagingSource = TrendingPagingSource(apiService)

    @Singleton
    @Provides
    fun provideSearchPagingSource(apiService: ApiService):
            SearchPagingSource = SearchPagingSource(apiService)


}