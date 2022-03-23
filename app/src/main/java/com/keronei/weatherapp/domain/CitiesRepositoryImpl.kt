package com.keronei.weatherapp.domain

import com.keronei.weatherapp.data.local.CityDao
import com.keronei.weatherapp.data.model.CityObjEntity

class CitiesRepositoryImpl(private val cityDao: CityDao) : CitiesRepository {

    override suspend fun addCity(cityObjEntity: CityObjEntity) = cityDao.createCity(cityObjEntity)

    override fun queryAllCities() = cityDao.queryAllCities()

    override fun queryLimitedCitiesCount(count: Int, country : String) = cityDao.queryLimitedCitiesCount(count, country)
}