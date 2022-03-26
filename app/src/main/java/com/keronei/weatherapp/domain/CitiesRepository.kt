package com.keronei.weatherapp.domain

import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    suspend fun addCity(cityObjEntity: CityObjEntity): Long

    fun queryAllCities(): Flow<List<CityWithForecast>>

    fun queryLimitedCitiesCount(count: Int, country: String): Flow<List<CityWithForecast>>
}