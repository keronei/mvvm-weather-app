package com.keronei.weatherapp.domain

import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCitiesRepositoryImpl : CitiesRepository {

    private val citiesStore = mutableListOf<CityObjEntity>()

    private val citiesStoreWithForecast = mutableListOf<CityWithForecast>()

    override suspend fun addCity(cityObjEntity: CityObjEntity): Long {
        citiesStore.add(cityObjEntity)
        return citiesStore.indexOf(cityObjEntity).toLong()
    }

    override fun queryAllCities(): Flow<List<CityWithForecast>> {
        return flowOf(citiesStoreWithForecast)
    }

    override fun queryLimitedCitiesCount(
        count: Int,
        country: String
    ): Flow<List<CityWithForecast>> {
        return flowOf(citiesStoreWithForecast.filter { cityWithForecast -> cityWithForecast.cityObjEntity.name == country }
            .take(count))
    }

}