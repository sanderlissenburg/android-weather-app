package dev.lissenburg.weatherapp

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.lissenburg.weatherapp.data.WeatherApiService
import dev.lissenburg.weatherapp.data.db.CurrentWeatherDao
import dev.lissenburg.weatherapp.data.db.FutureWeatherDao
import dev.lissenburg.weatherapp.data.db.WeatherDatabase
import dev.lissenburg.weatherapp.data.db.WeatherLocationDao
import dev.lissenburg.weatherapp.data.network.BasicConnectivityInterceptor
import dev.lissenburg.weatherapp.data.network.ConnectivityInterceptor
import dev.lissenburg.weatherapp.data.network.NetworkWeatherDataSource
import dev.lissenburg.weatherapp.data.network.WeatherDataSource
import dev.lissenburg.weatherapp.data.provider.BasicLocationProvider
import dev.lissenburg.weatherapp.data.provider.LocationProvider
import dev.lissenburg.weatherapp.data.provider.UnitFromSettingsProvider
import dev.lissenburg.weatherapp.data.provider.UnitProvider
import dev.lissenburg.weatherapp.data.repository.WeatherRepository
import dev.lissenburg.weatherapp.data.repository.HttpWeatherRepository
import dev.lissenburg.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import dev.lissenburg.weatherapp.ui.weather.future.detail.FutureDetailWeatherViewModelFactory
import dev.lissenburg.weatherapp.ui.weather.future.list.FutureListWeatherViewModelFactory
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

class WeatherApplication : Application(), DIAware {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    override val di by DI.lazy {
        import(androidXModule(this@WeatherApplication))

        bind<WeatherDatabase>() with singleton { WeatherDatabase(instance()) }
        bind<CurrentWeatherDao>() with singleton { instance<WeatherDatabase>().currentWeatherDao() }
        bind<FutureWeatherDao>() with singleton { instance<WeatherDatabase>().futureWeatherDao() }
        bind<WeatherLocationDao>() with singleton { instance<WeatherDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { BasicConnectivityInterceptor(instance()) }
        bind<WeatherApiService>() with singleton { WeatherApiService(instance()) }
        bind<WeatherDataSource>() with singleton { NetworkWeatherDataSource(instance()) }
        bind<WeatherRepository>() with singleton { HttpWeatherRepository(instance(), instance(), instance(), instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitFromSettingsProvider(instance()) }
        bind<FusedLocationProviderClient>() with provider { LocationServices.getFusedLocationProviderClient(instance<Context>())}
        bind<LocationProvider>() with singleton { BasicLocationProvider(instance(), instance()) }
        bind<CurrentWeatherViewModelFactory>() with  provider { CurrentWeatherViewModelFactory(instance(), instance())}
        bind<FutureListWeatherViewModelFactory>() with  provider { FutureListWeatherViewModelFactory(instance(), instance())}
        bind<FutureDetailWeatherViewModelFactory>() with factory { id: Int ->  FutureDetailWeatherViewModelFactory(id, instance(), instance())}
    }
}