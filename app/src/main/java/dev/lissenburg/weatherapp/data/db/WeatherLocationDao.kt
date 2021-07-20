package dev.lissenburg.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.lissenburg.weatherapp.data.db.entity.WEATHER_LOCATION_ID
import dev.lissenburg.weatherapp.data.db.entity.WeatherLocation

@Dao
interface WeatherLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entry: WeatherLocation)

    @Query("SELECT * FROM weather_location WHERE id=$WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<WeatherLocation>
}