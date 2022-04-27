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
package com.keronei.weatherapp.domain

import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.local.ForecastDao
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.data.model.ForecastUpdate
import com.keronei.weatherapp.data.remote.NetworkDataSource
import com.keronei.weatherapp.utils.ConnectivityProvider
import com.keronei.weatherapp.utils.fromUnixTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ForecastRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val forecastDao: ForecastDao,
    private val connectivityProvider: ConnectivityProvider
) :
    ForecastRepository {
    override suspend fun fetchCityForecast(cityWithForecast: CityWithForecast) =
        callbackFlow {
            trySend(Resource.Loading)

            val lat = cityWithForecast.cityObjEntity.lat
            val lon = cityWithForecast.cityObjEntity.lng
            val cityId = cityWithForecast.cityObjEntity.identity

            val localResource = cityWithForecast.forecast

            val lastDateTimestamp: Int = if (localResource == null) {
                0
            } else {
                localResource.daily.maxByOrNull { day -> day.dt }?.dt ?: 0
            }
            val currentTimestamp = Calendar.getInstance().time

            val validUntilDate = Date((lastDateTimestamp.fromUnixTimestamp()))

            val forecastIsExpired = currentTimestamp.after(validUntilDate)

            if (forecastIsExpired) {
                if (connectivityProvider.isNetworkAvailable()) {
                    // 1. Query
                    val forecastResource = networkDataSource.getCityForecast(lat, lon)

                    forecastResource.collect { result ->
                        when (result) {
                            is Resource.Failure -> {
                                if (localResource == null) {
                                    trySend(Resource.Empty)
                                } else {
                                    trySend(result)
                                }
                            }
                            Resource.Loading -> {
                                trySend(result)
                            }
                            is Resource.Success -> {
                                // Update previous data
                                if (localResource != null) {
                                    withContext(Dispatchers.IO) {
                                        forecastDao.updateForecast(
                                            ForecastUpdate(
                                                localResource.id,
                                                result.data.alerts,
                                                result.data.daily,
                                                result.data.hourly
                                            )
                                        )
                                    }
                                } else {
                                    // 2. Persist
                                    result.data.cityId = cityId

                                    forecastDao.createForecastUpdate(result.data)
                                }
                                // 3. Offer from local
                                forecastDao.getForecast(lat, lon)
                                    .collectLatest { updatedFromLocal ->
                                        if (updatedFromLocal.isNotEmpty()) {
                                            trySend(Resource.Success(updatedFromLocal.first()))
                                        }
                                    }
                            }
                            else -> {
                                trySend(Resource.Empty)
                            }
                        }
                    }
                } else {
                    if (localResource == null) {
                        trySend(Resource.Empty)
                    } else {
                        trySend(Resource.Outdated(localResource))
                    }
                }
            } else {
                if (localResource == null) {
                    trySend(Resource.Empty)
                } else {
                    trySend(Resource.Outdated(localResource))
                }
            }

            awaitClose { close() }
        }
}