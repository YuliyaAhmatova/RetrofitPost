package com.example.retrofitpost

import com.example.retrofitpost.models.ApiData
import retrofit2.http.GET

interface ApiInterface {
    @GET("woof.json?ref=apilist.fun")
    suspend fun getRandomDog(): ApiData
}