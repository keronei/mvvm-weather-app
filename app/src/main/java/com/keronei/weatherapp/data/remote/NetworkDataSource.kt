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
                    trySend(Resource.Failure(Exception("Failure")))
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