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

import com.keronei.weatherapp.application.Constants.DEFAULT_FIELDS_TO_EXCLUDE
import com.keronei.weatherapp.application.Constants.ONE_CALL_FORECAST_ENDPOINT
import com.keronei.weatherapp.data.model.Forecast
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(ONE_CALL_FORECAST_ENDPOINT)
    suspend fun getCityWeatherForeCast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") fieldsToExclude: String = DEFAULT_FIELDS_TO_EXCLUDE
    ): Forecast?
}