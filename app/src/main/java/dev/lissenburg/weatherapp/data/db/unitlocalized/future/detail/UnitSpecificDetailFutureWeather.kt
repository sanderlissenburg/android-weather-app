package dev.lissenburg.weatherapp.data.db.unitlocalized.future.detail

import org.threeten.bp.LocalDate

interface UnitSpecificDetailFutureWeather {
    val id: Int
    val date: LocalDate
    val maxTemperature: Double
    val minTemperature: Double
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val maxWindSpeed: Double
    val totalPrecipitation: Double
    val avgVisibilityDistance: Double
    val uv: Double
}