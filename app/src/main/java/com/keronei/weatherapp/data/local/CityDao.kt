package com.keronei.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.keronei.weatherapp.data.model.CityObjEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert(onConflict = REPLACE)
    suspend fun createCity(city: CityObjEntity): Long

    @Query("SELECT * FROM CityObjEntity ORDER BY id ASC")
    fun queryAllCities(): Flow<List<CityObjEntity>>

    @Query("SELECT * FROM CityObjEntity WHERE favourite = 1 or country LIKE :country || '%' or id > 0 ORDER BY favourite DESC LIMIT :count")
    fun queryLimitedCitiesCount(count: Int, country: String): Flow<List<CityObjEntity>>

    @Update(entity = CityObjEntity::class)
    fun toggleFavourite(city: CityObjEntity): Int
}