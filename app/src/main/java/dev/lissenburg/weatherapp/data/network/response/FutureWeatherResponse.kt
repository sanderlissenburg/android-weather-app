package dev.lissenburg.weatherapp.data.network.response


import com.google.gson.annotations.SerializedName
import dev.lissenburg.weatherapp.data.db.entity.WeatherLocation

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val futureWeatherEntries: ForecastDaysContainer,
    val location: WeatherLocation
)