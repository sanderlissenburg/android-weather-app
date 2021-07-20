package dev.lissenburg.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.lissenburg.weatherapp.data.db.entity.FutureWeatherEntry
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.detail.ImperialDetailFutureWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.detail.MetricDetailFutureWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.list.ImperialSimpleFutureWeather
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.list.MetricSimpleFutureWeather
import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: FutureWeatherEntry)

    @Query("SELECT * FROM future_weather WHERE date(date) >= date(:startDate)")
    fun getSimpleFutureWeatherImperial(startDate: LocalDate): LiveData<List<ImperialSimpleFutureWeather>>

    @Query("SELECT * FROM future_weather WHERE date(date) >= date(:startDate)")
    fun getSimpleFutureWeatherMetric(startDate: LocalDate): LiveData<List<MetricSimpleFutureWeather>>

    @Query("SELECT count(id) FROM future_weather WHERE date(date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("DELETE FROM future_weather WHERE date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)

    @Query("SELECT * FROM future_weather WHERE id = :id")
    fun getDetailFutureWeatherImperial(id: Int): LiveData<ImperialDetailFutureWeather>

    @Query("SELECT * FROM future_weather WHERE id = :id")
    fun getDetailFutureWeatherMetric(id: Int): LiveData<MetricDetailFutureWeather>
}