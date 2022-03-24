package com.keronei.weatherapp.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.keronei.weatherapp.data.model.Forecast
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = REPLACE)
    suspend fun createForecastUpdate(forecast: Forecast): Long

    @Query("SELECT * FROM Forecast WHERE lat = :lat AND lon = :lon LIMIT 1")
    fun getForecast(lat : Double, lon : Double) : Flow<List<Forecast>>

    @Delete
    fun deleteForecast(forecast: Forecast) : Int

}