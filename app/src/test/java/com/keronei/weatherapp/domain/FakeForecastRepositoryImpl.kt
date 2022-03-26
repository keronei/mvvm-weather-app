package com.keronei.weatherapp.domain

import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.data.model.Forecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FakeForecastRepositoryImpl : ForecastRepository{
    override suspend fun fetchCityForecast(cityWithForecast: CityWithForecast): Flow<Resource<Forecast>> = callbackFlow {
        val validResponse = MockData.getForecastResponse()

        trySend(Resource.Success(validResponse))

        awaitClose { close() }

    }

}