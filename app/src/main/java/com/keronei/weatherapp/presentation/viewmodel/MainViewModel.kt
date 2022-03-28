/*
 * Copyright 2022 GradleBuildPlugins
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
package com.keronei.weatherapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.keronei.weatherapp.application.Constants
import com.keronei.weatherapp.application.Constants.FIRST_COUNT
import com.keronei.weatherapp.application.Constants.NOTIFICATION_MANAGER_TAG
import com.keronei.weatherapp.application.Constants.WORK_ID
import com.keronei.weatherapp.core.worker.NotificationWorker
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.ForecastRepository
import com.keronei.weatherapp.domain.mappers.CityObjEntityToCityPresentationWithoutDataMapper
import com.keronei.weatherapp.presentation.CityPresentation
import com.keronei.weatherapp.ui.viewstate.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    private val forecastRepository: ForecastRepository,
    @ApplicationContext context: Context
) :
    ViewModel() {

    private val _cities = MutableStateFlow<ViewState>(value = ViewState.Empty)
    val cities: StateFlow<ViewState> = _cities

    private val first20Cities = mutableListOf<CityWithForecast>()

    private var hasFetchedForFirst20Already = false

    var selectedCity: CityWithForecast? = null

    private val outputWorkInfos: LiveData<List<WorkInfo>>

    private val workManager = WorkManager.getInstance(context)

    init {
        // Launch work manager job if there's any favourite
        // Also, if there's favourite city
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(NOTIFICATION_MANAGER_TAG)

        if (outputWorkInfos.value.isNullOrEmpty()) {
            initialiseJobToNotifyOfFavourite()
        }

    }

    private fun initialiseJobToNotifyOfFavourite() {
        val notifyTemperatureWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
                .addTag(NOTIFICATION_MANAGER_TAG).build()

        workManager.enqueueUniquePeriodicWork(
            WORK_ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            notifyTemperatureWorkRequest
        )
    }


    fun setSelectedCity(cityPresentation: CityPresentation) {
        selectedCity = first20Cities.firstOrNull { cityForecast ->
            cityForecast.cityObjEntity.identity == cityPresentation.id
        }
    }

    fun loadFirstTwentyCitiesFromCountry(country: String) {
        viewModelScope.launch(Dispatchers.Default) {
            citiesRepository.queryLimitedCitiesCount(
                FIRST_COUNT,
                country.uppercase(Locale.getDefault())
            ).collect { citiesObject ->

                val citiesAsPresentationWithData =
                    CityObjEntityToCityPresentationWithoutDataMapper().mapList(
                        citiesObject, viewModelScope
                    )
                first20Cities.clear()

                first20Cities.addAll(citiesObject)

                _cities.emit(ViewState.Success(citiesAsPresentationWithData))

                if (!hasFetchedForFirst20Already) {
                    fetchCitiesWeatherData()
                }
            }
        }
    }

    private fun fetchCitiesWeatherData() {
        hasFetchedForFirst20Already = true
        if (first20Cities.isNotEmpty()) {
            viewModelScope.launch {
                first20Cities.forEach { city ->
                    fetchForecastDataForCity(city.cityObjEntity.identity)
                }
            }
        }
    }

    fun fetchForecastDataForCity(cityId: Int) {
        val city: CityWithForecast =
            first20Cities.first { thisCity -> thisCity.cityObjEntity.identity == cityId }

        viewModelScope.launch {
            try {
                forecastRepository.fetchCityForecast(city)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}