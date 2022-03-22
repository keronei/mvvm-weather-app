package com.keronei.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.keronei.weatherapp.data.model.City

@Dao
interface CityDao {
    @Insert(onConflict = REPLACE)
    suspend fun createAllCities(city: City) : Long
}