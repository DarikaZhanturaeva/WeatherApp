package com.geeks.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geeks.weatherapp.data.model.CurrentWeather
import com.geeks.weatherapp.data.model.Weather
import com.geeks.weatherapp.data.repository.WeatherRepository
import com.geeks.weatherapp.utils.WeatherRetrofit
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    val weatherData = MutableLiveData<CurrentWeather>()
    val error = MutableLiveData<String>()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(city)
                if (response.isSuccessful && response.body() != null) {
                    weatherData.postValue(response.body()!!)
                } else {
                    error.postValue("Failed to fetch weather data")
                }
            } catch (e: Exception) {
                error.postValue("Error: ${e.message}")
            }
        }
    }
}