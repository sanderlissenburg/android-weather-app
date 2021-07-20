package dev.lissenburg.weatherapp.data.network

import androidx.lifecycle.LiveData
import dev.lissenburg.weatherapp.data.network.response.CurrentWeatherResponse
import dev.lissenburg.weatherapp.data.network.response.FutureWeatherResponse

interface WeatherDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        location: String,
        days: Int,
        languageCode: String
    )
}