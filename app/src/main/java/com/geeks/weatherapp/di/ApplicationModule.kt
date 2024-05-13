package com.geeks.weatherapp.di

import com.geeks.weatherapp.data.WeatherApiService
import com.geeks.weatherapp.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideApi(): WeatherApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WeatherApiService::class.java)
    }
    @Provides
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepository()
    }
}