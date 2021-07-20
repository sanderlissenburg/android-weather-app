package dev.lissenburg.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.lissenburg.weatherapp.data.db.entity.CURRENT_WEATHER_ID
import dev.lissenburg.weatherapp.data.db.entity.CurrentWeatherEntry
import dev.lissenburg.weatherapp.data.db.unitlocalized.current.ImperialCurrentWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.current.MetricCurrentWeather

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entry: CurrentWeatherEntry)

    @Query("SELECT * FROM current_weather WHERE id=$CURRENT_WEATHER_ID")
    fun getCurrentWeatherMetric(): LiveData<MetricCurrentWeather>

    @Query("SELECT * FROM current_weather WHERE id=$CURRENT_WEATHER_ID")
    fun getCurrentWeatherImperial(): LiveData<ImperialCurrentWeather>
}