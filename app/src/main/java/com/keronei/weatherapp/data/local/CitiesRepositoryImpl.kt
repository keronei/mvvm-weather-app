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

import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.domain.CitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(private val cityDao: CityDao) : CitiesRepository {

    override suspend fun addCity(cityObjEntity: CityObjEntity) = cityDao.createCity(cityObjEntity)

    override fun queryAllCities() = cityDao.queryAllCities()

    override fun queryFavouritedCities(): Flow<List<CityWithForecast>> =
        cityDao.queryFavouritedCitiesCount()

    override fun queryLimitedCitiesCount(count: Int, country: String) =
        cityDao.queryLimitedCitiesCount(count, country)

    override suspend fun toggleFavourite(cityObjEntity: CityObjEntity) {
        cityDao.toggleFavourite(cityObjEntity)
    }
}