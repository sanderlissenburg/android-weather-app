package dev.lissenburg.weatherapp.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import dev.lissenburg.weatherapp.data.provider.UnitProvider
import dev.lissenburg.weatherapp.data.repository.WeatherRepository
import dev.lissenburg.weatherapp.internal.UnitSystem
import dev.lissenburg.weatherapp.internal.lazyDeferred

class FutureDetailWeatherViewModel(
    private val id: Int,
    private val forecastRepository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getFutureWeather(id, isMetric)
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}