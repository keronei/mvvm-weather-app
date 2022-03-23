package com.keronei.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.keronei.weatherapp.data.model.Forecast

@Dao
interface ForecastDao {
    @Insert(onConflict = REPLACE)
    suspend fun createForecastUpdate(forecast: Forecast) : Long
}