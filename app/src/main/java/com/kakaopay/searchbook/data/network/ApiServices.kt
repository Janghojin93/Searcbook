package com.kakaopay.searchbook.data.network

import com.kakaopay.searchbook.BuildConfig
import com.kakaopay.searchbook.data.model.responce.SearchBookResponce
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiServices {

    @Headers("Authorization:KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("/v3/search/book")
    suspend fun getSearchBook(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchBookResponce>

}