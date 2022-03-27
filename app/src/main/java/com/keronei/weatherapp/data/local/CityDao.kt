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
    @Query("SELECT * FROM CityObjEntity WHERE identity > 0 or iso2 LIKE '%' || :country || '%' or  favourite = 1 ORDER BY favourite DESC LIMIT :count")
    fun queryLimitedCitiesCount(count: Int, country: String): Flow<List<CityWithForecast>>

    @Update(entity = CityObjEntity::class)
    fun toggleFavourite(city: CityObjEntity): Int
}