package dev.lissenburg.weatherapp.data.network.response

import com.google.gson.annotations.SerializedName
import dev.lissenburg.weatherapp.data.db.entity.CurrentWeatherEntry
import dev.lissenburg.weatherapp.data.db.entity.WeatherLocation


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeather: CurrentWeatherEntry,
    val location: WeatherLocation
)