package com.geeks.weatherapp.data

import com.geeks.weatherapp.data.model.CurrentWeather
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = "66872f52afa125bd3d0da920ca18d9f7"
    ): Response<CurrentWeather>
}