package dev.lissenburg.weatherapp.data.repository

import androidx.lifecycle.LiveData
import dev.lissenburg.weatherapp.data.db.CurrentWeatherDao
import dev.lissenburg.weatherapp.data.db.FutureWeatherDao
import dev.lissenburg.weatherapp.data.db.WeatherLocationDao
import dev.lissenburg.weatherapp.data.db.entity.WeatherLocation
import dev.lissenburg.weatherapp.data.db.unitlocalized.current.UnitSpecificCurrentWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeather
import dev.lissenburg.weatherapp.data.network.WeatherDataSource
import dev.lissenburg.weatherapp.data.network.response.CurrentWeatherResponse
import dev.lissenburg.weatherapp.data.network.response.FutureWeatherResponse
import dev.lissenburg.weatherapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

class HttpWeatherRepository(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherDataSource: WeatherDataSource,
    private val weatherLocationDao: WeatherLocationDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val locationProvider: LocationProvider
) : WeatherRepository {

    init {
        weatherDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }

        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeather> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext if (metric) currentWeatherDao.getCurrentWeatherMetric() else currentWeatherDao.getCurrentWeatherImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<out WeatherLocation> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    override suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeather>> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext if (metric) futureWeatherDao.getSimpleFutureWeatherMetric(startDate) else futureWeatherDao.getSimpleFutureWeatherImperial(startDate)
        }
    }

    override suspend fun getFutureWeather(id: Int, metric: Boolean): LiveData<out UnitSpecificDetailFutureWeather> {
        return withContext(Dispatchers.IO) {
            return@withContext if (metric) futureWeatherDao.getDetailFutureWeatherMetric(id) else futureWeatherDao.getDetailFutureWeatherImperial(id)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedCurrentWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedCurrentWeather.currentWeather)
            weatherLocationDao.upsert(fetchedCurrentWeather.location)
        }
    }

    private fun persistFetchedFutureWeather(fetchedFutureWeather: FutureWeatherResponse) {
        fun deleteOld() {
            futureWeatherDao.deleteOldEntries(LocalDate.now())
        }
        GlobalScope.launch(Dispatchers.IO) {
            futureWeatherDao.deleteOldEntries(LocalDate.now())
            fetchedFutureWeather.futureWeatherEntries.entries.forEach { entry ->
                futureWeatherDao.insert(entry)
            }
            weatherLocationDao.upsert(fetchedFutureWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if (
            lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zoneDateTime)) {
            fetchCurrentWeather()
        }

        if (isFetchFutureNeeded()) {
            fetchFutureWeather()
        }
    }

    private suspend fun fetchCurrentWeather() {
        weatherDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationString(), Locale.getDefault().language)
    }

    private suspend fun fetchFutureWeather() {
        weatherDataSource.fetchFutureWeather(locationProvider.getPreferredLocationString(), 7, Locale.getDefault().language)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val currentCount = futureWeatherDao.countFutureWeather(today)
        return currentCount < 7
    }
}