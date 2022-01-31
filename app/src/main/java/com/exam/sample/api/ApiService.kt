package com.exam.sample.api

import com.exam.sample.model.data.TrendingData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("gifs/trending")
    fun getTrendingDataRequest(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String
    ): Single<TrendingData>

    @GET("gifs/random")
    suspend fun getRandomDataRequestFlow(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String
    ): TrendingData

    @GET("gifs/trending")
    suspend fun getTrendingDataRequestFlow(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String
    ): TrendingData

//    @GET("gifs/trending")
//    fun getTrendingDataRequest(
//        @Query("api_key") apiKey: String,
//        @Query("limit") limit: Int,
//        @Query("offset") offset: Int,
//        @Query("rating") rating: String
//    ): Single<TrendingData>


    @GET("gifs/trending")
    fun getDetailData(
        @Query("api_key") apiKey: String,
        @Query("ids") ids: String
    ): Single<TrendingData>

    @GET("gifs/{gif_id}")
    fun getGIFByID(
        @Path("gif_id") gif_id: String,
        @Query("api_key") apiKey: String
    ): Single<TrendingData>

    @GET("gifs")
    fun getGIFsByIDs(
        @Query("api_key") apiKey: String,
        @Query("ids") ids: String
    ): Single<TrendingData>

    @GET("gifs/search")
    fun getSearchFromKeyword(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("q") q: String
    ): Single<TrendingData>

    @GET("gifs/search")
    suspend fun getSearchFromKeywordFlow(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("q") q: String
    ): TrendingData
}
