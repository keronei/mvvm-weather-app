/*
 * Copyright 2022 GradleBuildPlugins
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
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert(onConflict = REPLACE)
    suspend fun createCity(city: CityObjEntity): Long

    @Query("SELECT * FROM CityObjEntity ORDER BY identity ASC")
    fun queryAllCities(): Flow<List<CityWithForecast>>

    @Transaction
    @Query("SELECT * FROM CityObjEntity WHERE favourite = 1 OR (iso2 LIKE '%' || :country || '%') ORDER BY favourite DESC LIMIT :count")
    fun queryLimitedCitiesCount(count: Int, country: String): Flow<List<CityWithForecast>>

    @Update(entity = CityObjEntity::class)
    suspend fun toggleFavourite(city: CityObjEntity): Int

    @Transaction
    @Query("SELECT * FROM CityObjEntity WHERE favourite = 1")
    fun queryFavouritedCitiesCount(): Flow<List<CityWithForecast>>
}