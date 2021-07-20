package dev.lissenburg.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.lissenburg.weatherapp.data.WeatherApiService
import dev.lissenburg.weatherapp.data.network.response.CurrentWeatherResponse
import dev.lissenburg.weatherapp.data.network.response.FutureWeatherResponse
import dev.lissenburg.weatherapp.internal.NoConnectivityException

class NetworkWeatherDataSource(
    private val weatherApiService: WeatherApiService
) : WeatherDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val response = weatherApiService.getCurrentWeather(
                location,
                languageCode
            )

            _downloadedCurrentWeather.postValue(response)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    override suspend fun fetchFutureWeather(location: String, days: Int, languageCode: String) {
        try {
            val response = weatherApiService.getFutureWeather(
                location,
                days,
                languageCode
            )

            _downloadedFutureWeather.postValue(response)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}