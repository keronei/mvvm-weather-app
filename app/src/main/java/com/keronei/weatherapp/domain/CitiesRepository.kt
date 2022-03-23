package com.keronei.weatherapp.domain

import com.keronei.weatherapp.data.model.CityObjEntity
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    suspend fun addCity(cityObjEntity: CityObjEntity) : Long

    fun queryAllCities(): Flow<List<CityObjEntity>>

    fun queryLimitedCitiesCount(count: Int): Flow<List<CityObjEntity>>
}