/*
 * Copyright 2022 Keronei Lincoln
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.weatherapp.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.data.model.ForecastUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = REPLACE)
    suspend fun createForecastUpdate(forecast: Forecast): Long

    @Query("SELECT * FROM Forecast WHERE lat = :lat AND lon = :lon LIMIT 1")
    fun getForecast(lat: Double, lon: Double): Flow<List<Forecast>>

    @Update(entity = Forecast::class) // partial update
    fun updateForecast(forecastUpdate: ForecastUpdate): Int
}