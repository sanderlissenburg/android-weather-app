package dev.lissenburg.weatherapp.data.provider

import dev.lissenburg.weatherapp.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}