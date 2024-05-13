package com.geeks.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    var deg: Int? = null,
    @SerializedName("gust")
    var gust: Double? = null,
    @SerializedName("speed")
    var speed: Double? = null
)