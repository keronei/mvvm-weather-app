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