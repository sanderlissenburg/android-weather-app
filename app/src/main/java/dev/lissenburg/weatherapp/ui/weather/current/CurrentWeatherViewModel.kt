package dev.lissenburg.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import dev.lissenburg.weatherapp.data.provider.UnitProvider
import dev.lissenburg.weatherapp.data.repository.WeatherRepository
import dev.lissenburg.weatherapp.internal.UnitSystem
import dev.lissenburg.weatherapp.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        weatherRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeferred {
        weatherRepository.getWeatherLocation()
    }
}