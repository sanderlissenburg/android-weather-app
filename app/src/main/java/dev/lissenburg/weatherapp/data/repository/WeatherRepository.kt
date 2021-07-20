package dev.lissenburg.weatherapp.data.repository

import androidx.lifecycle.LiveData
import dev.lissenburg.weatherapp.data.db.entity.WeatherLocation
import dev.lissenburg.weatherapp.data.db.unitlocalized.current.UnitSpecificCurrentWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeather
import org.threeten.bp.LocalDate

interface WeatherRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeather>
    suspend fun getWeatherLocation(): LiveData<out WeatherLocation>
    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeather>>
    suspend fun getFutureWeather(id: Int, metric: Boolean): LiveData<out UnitSpecificDetailFutureWeather>
}