package com.keronei.weatherapp.domain

import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import junit.framework.TestCase
import kotlinx.coroutines.flow.Flow

class CitiesRepositoryImplTest : CitiesRepository {


    override suspend fun addCity(cityObjEntity: CityObjEntity): Long {

    }

    override fun queryAllCities(): Flow<List<CityWithForecast>> {

    }

    override fun queryLimitedCitiesCount(
        count: Int,
        country: String
    ): Flow<List<CityWithForecast>> {

    }

}