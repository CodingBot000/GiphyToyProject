package com.exam.sample.di

import com.exam.sample.model.datasource.local.favorite.FavoriteInfoLocalDataSource
import com.exam.sample.model.datasource.remote.artist.ArtistPagingSource
import com.exam.sample.model.datasource.remote.clip.ClipPagingSource
import com.exam.sample.model.datasource.remote.detail.DetailDataRemoteDataSource
import com.exam.sample.model.datasource.remote.favorite.FavoriteInfoRemoteDataSource
import com.exam.sample.model.datasource.remote.search.*
import com.exam.sample.model.datasource.remote.trending.TrendingPagingSource
import com.exam.sample.model.datasource.remote.trending.TrendingRemoteDataSource
import com.exam.sample.model.repository.detail.DetailDataRepository
import com.exam.sample.model.repository.detail.DetailDataRepositoryImpl
import com.exam.sample.model.repository.favorite.FavoriteInfoRepository
import com.exam.sample.model.repository.favorite.FavoriteInfoRepositoryImpl
import com.exam.sample.model.repository.search.*
import com.exam.sample.model.repository.trending.TrendingPagingRepository
import com.exam.sample.model.repository.trending.TrendingRepository
import com.exam.sample.model.repository.trending.TrendingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideTrendingRepository(trendingRemoteDataSource: TrendingRemoteDataSource): TrendingRepository =
        TrendingRepositoryImpl(trendingRemoteDataSource)

    @Singleton
    @Provides
    fun provideSearchRespository(searchRemoteDataSource: SearchRemoteDataSource): SearchRepository =
        SearchRepositoryImpl(searchRemoteDataSource)

    @Singleton
    @Provides
    fun provideDetailDataRepository(
        detailDataRemoteDataSource: DetailDataRemoteDataSource,
        favoriteInfoLocalDataSource: FavoriteInfoLocalDataSource
    ): DetailDataRepository =
        DetailDataRepositoryImpl(detailDataRemoteDataSource, favoriteInfoLocalDataSource)

    @Singleton
    @Provides
    fun provideFavoriteInfoRepository(
        favoriteInfoRemoteDataSource: FavoriteInfoRemoteDataSource,
        favoriteInfoLocalDataSource: FavoriteInfoLocalDataSource
    ): FavoriteInfoRepository =
        FavoriteInfoRepositoryImpl(favoriteInfoRemoteDataSource, favoriteInfoLocalDataSource)


    @Singleton
    @Provides
    fun provideTrendingPagingRepository(
        trendingPagingSource: TrendingPagingSource
    ): TrendingPagingRepository =
        TrendingPagingRepository(trendingPagingSource)

    @Singleton
    @Provides
    fun provideArtistPagingRepository(
        artistPagingSource: ArtistPagingSource
    ): ArtistPagingRepository =
        ArtistPagingRepository(artistPagingSource)

    @Singleton
    @Provides
    fun provideClipPagingRepository(
        clipPagingSource: ClipPagingSource
    ): ClipPagingRepository =
        ClipPagingRepository(clipPagingSource)

//    @Singleton
    @Provides
    fun provideSearchPagingRepository(
        searchPagingSource: SearchPagingSource
    ): SearchPagingRepository =
        SearchPagingRepository(searchPagingSource)
}