package com.keronei.weatherapp.data.remote

import com.keronei.weatherapp.application.Constants.ONE_CALL_FORECAST_ENDPOINT
import retrofit2.http.GET

interface ApiService {
    @GET(ONE_CALL_FORECAST_ENDPOINT)
    suspend fun getCityWeatherForeCast()
}