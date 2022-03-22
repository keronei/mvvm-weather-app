package com.keronei.weatherapp.data.remote

import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.CityForecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getCityForecast(): Flow<Resource<CityForecast>> = callbackFlow {
        trySend(Resource.Success(apiService.getCityWeatherForeCast()))

        awaitClose { close() }
    }
}