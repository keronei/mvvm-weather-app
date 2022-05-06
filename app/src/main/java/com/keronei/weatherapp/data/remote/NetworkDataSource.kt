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
package com.keronei.weatherapp.data.remote

import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.Forecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getCityForecast(lat: Double, lon: Double): Flow<Resource<Forecast>> = callbackFlow {
        try {
            trySend(Resource.Loading)

            val cityForeCast = apiService.getCityWeatherForeCast(lat, lon)
            when {
                cityForeCast != null -> {
                    trySend(Resource.Success(cityForeCast))
                }
                else -> {
                    trySend(Resource.Failure(Exception("Forecast data is null.")))
                }
            }
        } catch (e: IOException) {
            trySend(Resource.Failure(e))
            Timber.e(e)
        } catch (e: Exception) {
            Timber.e(e)
            trySend(Resource.Failure(e))
        }

        awaitClose { close() }
    }
}