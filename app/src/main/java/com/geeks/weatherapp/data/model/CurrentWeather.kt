package com.geeks.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("base")
    var base: String? = null,
    @SerializedName("clouds")
    var clouds: Clouds = Clouds(),
    @SerializedName("cod")
    var cod: Int? = null,
    @SerializedName("coord")
    var coord: Coord = Coord(),
    @SerializedName("dt")
    var dt: Int? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("main")
    var main: Main = Main(),
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("sys")
    var sys: Sys = Sys(),
    @SerializedName("timezone")
    var timezone: Int? = null,
    @SerializedName("visibility")
    var visibility: Int? = null,
    @SerializedName("weather")
    var weather: ArrayList<Weather>,
    @SerializedName("wind")
    var wind: Wind = Wind()
)