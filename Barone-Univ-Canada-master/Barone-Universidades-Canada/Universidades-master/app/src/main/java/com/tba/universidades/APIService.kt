package com.tba.universidades

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("search")
    fun getUniByName(@Query("country") country: String, @Query("name") name: String): Call<List<Universidad>>

    @GET("search")
    fun getAllUniversities(@Query("country") country: String): Call<List<Universidad>>
}