package com.geeks.weatherapp.utils

import com.geeks.weatherapp.data.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRetrofit {

    val api: WeatherApiService by lazy {
        Retrofit.Builder().baseUrl(Util.Base)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherApiService::class.java)
    }

}