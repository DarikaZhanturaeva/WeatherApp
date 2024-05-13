package com.geeks.weatherapp.data.repository

import com.geeks.weatherapp.data.WeatherApiService
import com.geeks.weatherapp.utils.WeatherRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository //@Inject constructor(
    //private val api : WeatherApiService
    () {
    suspend fun getCurrentWeather(city: String) = withContext(Dispatchers.IO) {
        WeatherRetrofit.api.getCurrentWeather(city, "metric", "your_api_key")
    }
}