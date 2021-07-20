package dev.lissenburg.weatherapp.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dev.lissenburg.weatherapp.internal.UnitSystem

class UnitFromSettingsProvider(context: Context) : PreferencesProvider(context), UnitProvider {
    override fun getUnitSystem(): UnitSystem {
        return UnitSystem.valueOf(preferences.getString("UNIT_SYSTEM", UnitSystem.METRIC.name)!!)
    }
}