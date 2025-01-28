package com.example.trainingroutine_pablocavaz.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleMapsRetrofitInstance {
    private const val BASE_URL = "https://maps.googleapis.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
