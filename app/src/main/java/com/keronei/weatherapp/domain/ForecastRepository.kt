package com.keronei.weatherapp.domain

import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.Forecast
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    suspend fun fetchCityForecast(lat: Double, lon: Double): Flow<Resource<Forecast>>
}