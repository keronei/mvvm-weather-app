package com.keronei.weatherapp.domain

import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.local.ForecastDao
import com.keronei.weatherapp.data.remote.NetworkDataSource
import com.keronei.weatherapp.utils.ConnectivityProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ForecastRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val forecastDao: ForecastDao,
    private val connectivityProvider: ConnectivityProvider
) :
    ForecastRepository {
    override suspend fun fetchCityForecast(lat: Double, lon: Double) =
        callbackFlow {
            trySend(Resource.Loading)

            val localData = forecastDao.getForecast(lat, lon)

            val localResource = localData.first()

            val lastDateTimestamp: Int = if (localResource.isEmpty()) {
                0
            } else {
                localResource.first().daily.maxByOrNull { day -> day.dt }?.dt ?: 0
            }
            val currentTimestamp = Calendar.getInstance().time

            val validUntilDate = Date((lastDateTimestamp * 1000L))

            // TODO establish why time is provided in value that needs *1000
            val forecastIsExpired = currentTimestamp.after(validUntilDate)

            if (forecastIsExpired) {
                if (connectivityProvider.isNetworkAvailable()) {
                    // 1. Query
                    val forecastResource = networkDataSource.getCityForecast(lat, lon)

                    forecastResource.collect { result ->
                        when (result) {
                            is Resource.Failure -> {
                                if (localResource.isEmpty()) {
                                    trySend(Resource.Empty)
                                } else {
                                    trySend(Resource.Success(localResource.first()))
                                }
                            }
                            Resource.Loading -> {
                                trySend(result)
                            }
                            is Resource.Success -> {
                                // First delete previous update
                                if (localResource.isNotEmpty()) {
                                    withContext(Dispatchers.IO) {
                                        forecastDao.deleteForecast(localResource.first())
                                    }
                                }
                                // 2. Persist
                                forecastDao.createForecastUpdate(result.data)
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
                    if (localResource.isEmpty()) {
                        trySend(Resource.Empty)
                    } else {
                        trySend(Resource.Outdated(localResource.first()))
                    }
                }
            } else {
                if (localResource.isEmpty()) {
                    trySend(Resource.Empty)
                } else {
                    trySend(Resource.Outdated(localResource.first()))
                }
            }

            awaitClose { close() }
        }
}