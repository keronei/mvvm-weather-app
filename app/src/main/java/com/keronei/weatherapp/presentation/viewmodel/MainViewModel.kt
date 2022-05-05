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
package com.keronei.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.weatherapp.application.Constants.FIRST_COUNT
import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.ForecastRepository
import com.keronei.weatherapp.domain.mappers.CityObjEntityToCityPresentationWithoutDataMapper
import com.keronei.weatherapp.presentation.CityPresentation
import com.keronei.weatherapp.ui.viewstate.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    private val forecastRepository: ForecastRepository
) :
    ViewModel() {

    private val _cities = MutableStateFlow<ViewState>(value = ViewState.Empty)
    val cities: StateFlow<ViewState> = _cities

    private val first20Cities = mutableListOf<CityWithForecast>()

    private var hasFetchedForFirst20Already = false

    var selectedCity: CityWithForecast? = null

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

    fun refreshData() {
        fetchCitiesWeatherData()
    }

    private fun fetchCitiesWeatherData() {
        hasFetchedForFirst20Already = true
        if (first20Cities.isNotEmpty()) {
            viewModelScope.launch {
                first20Cities.forEach { city ->
                    fetchForecastDataForCity(city).collect()
                }
            }
        }
    }

    fun fetchForCityWithId(cityId: Int) {
        try {
            viewModelScope.launch {
                fetchForecastDataForCity(
                    first20Cities.first { cityWithForecast ->
                        cityWithForecast.cityObjEntity.identity == cityId
                    }
                ).collect()
            }

        }catch (exception: Exception){
            // when no matching element is found.
            exception.printStackTrace()
        }
    }

    fun fetchForecastDataForCity(city: CityWithForecast) = flow {
            try {
                forecastRepository.fetchCityForecast(city).collect { returnedResource ->
                    emit(returnedResource)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

}