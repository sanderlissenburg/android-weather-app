package dev.lissenburg.weatherapp.ui.weather.future.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.lissenburg.weatherapp.data.provider.UnitProvider
import dev.lissenburg.weatherapp.data.repository.WeatherRepository

class FutureDetailWeatherViewModelFactory(
    private val id: Int,
    private val weatherRepository : WeatherRepository,
    private val unitProvider: UnitProvider
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        Log.e("Debug", "Viewmodel with id: $id")

        return FutureDetailWeatherViewModel(id, weatherRepository, unitProvider) as T
    }
}