package dev.lissenburg.weatherapp.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import dev.lissenburg.weatherapp.data.db.entity.WeatherLocation
import dev.lissenburg.weatherapp.internal.LocationPermissionNotGrantedException
import dev.lissenburg.weatherapp.internal.asDeffered
import kotlinx.coroutines.Deferred

class BasicLocationProvider(
    context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : PreferencesProvider(context),  LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val hasDeviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            return false
        }

        return hasDeviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await() ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude}, ${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        } else {
            return "${getCustomLocationName()}"
        }
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await() ?: return false

        val comparisonTrashhold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonTrashhold
                || Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonTrashhold
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    {
        if (isUsingDeviceLocation())
            return false

        val customLocationName = getCustomLocationName()
        return customLocationName != lastWeatherLocation.name
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean("USE_DEVICE_LOCATION", true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        if (hasLocationPermission()) {
            // Check how this asDeferred magic/function extension works
            return fusedLocationProviderClient.lastLocation.asDeffered()
        }

        throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString("CUSTOM_LOCATION", null)
    }
}