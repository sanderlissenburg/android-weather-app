package dev.lissenburg.weatherapp.data.network.response


import com.google.gson.annotations.SerializedName
import dev.lissenburg.weatherapp.data.db.entity.FutureWeatherEntry

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)