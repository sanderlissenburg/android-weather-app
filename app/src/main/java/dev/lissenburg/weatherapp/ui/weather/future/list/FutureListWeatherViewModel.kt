package dev.lissenburg.weatherapp.ui.weather.future.list

import androidx.lifecycle.ViewModel
import dev.lissenburg.weatherapp.data.provider.UnitProvider
import dev.lissenburg.weatherapp.data.repository.WeatherRepository
import dev.lissenburg.weatherapp.internal.UnitSystem
import dev.lissenburg.weatherapp.internal.lazyDeferred
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val entries by lazyDeferred {
        weatherRepository.getFutureWeatherList(LocalDate.now(), isMetric)
    }

    val weatherLocation by lazyDeferred {
        weatherRepository.getWeatherLocation()
    }
}