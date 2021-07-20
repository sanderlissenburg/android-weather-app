package dev.lissenburg.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.lissenburg.weatherapp.data.provider.UnitProvider
import dev.lissenburg.weatherapp.data.repository.WeatherRepository

class CurrentWeatherViewModelFactory(
    private val weatherRepository : WeatherRepository,
    private val unitProvider: UnitProvider
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository, unitProvider) as T
    }
}