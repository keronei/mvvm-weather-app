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
import com.keronei.weatherapp.data.mappers.CityObjEntityToCityPresentationWithoutDataMapper
import com.keronei.weatherapp.presentation.CityPresentation
import com.keronei.weatherapp.ui.viewstate.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    private val _singleCityForecast = MutableStateFlow<Resource<Forecast>>(value = Resource.Empty)
    val singleCityWithForecast: StateFlow<Resource<Forecast>> = _singleCityForecast

    private var first20Cities = mutableListOf<CityWithForecast>()

    private var hasFetchedForFirst20Already = false

    var selectedCity: CityWithForecast? = null

    fun setSelectedCity(cityPresentation: CityPresentation) {
        selectedCity = first20Cities.firstOrNull { cityForecast ->
            cityForecast.cityObjEntity.identity == cityPresentation.id
        }
    }

    fun loadFirstTwentyCitiesFromCountry(country: String) =
        viewModelScope.launch(Dispatchers.Default) {
            citiesRepository.queryLimitedCitiesCount(
                FIRST_COUNT,
                country.uppercase(Locale.getDefault())
            ).collect { citiesObject ->

                val citiesAsPresentationWithData =
                    CityObjEntityToCityPresentationWithoutDataMapper().mapList(
                        citiesObject, viewModelScope
                    )

                first20Cities = citiesObject as MutableList<CityWithForecast>

                _cities.emit(ViewState.Success(citiesAsPresentationWithData))

                if (!hasFetchedForFirst20Already) {
                    fetchCitiesWeatherData()
                }
            }
        }

    fun refreshData() {
        fetchCitiesWeatherData()
    }

    private fun fetchCitiesWeatherData() {
        hasFetchedForFirst20Already = true
        if (first20Cities.isNotEmpty()) {
            first20Cities.forEach { city ->
                fetchForecastDataForCity(city)
            }
        }
    }

    fun fetchForCityWithId(cityId: Int) =
        try {
            fetchForecastDataForCity(
                first20Cities.first { cityWithForecast ->
                    cityWithForecast.cityObjEntity.identity == cityId
                }
            )
        } catch (exception: Exception) {
            // when no matching element is found.
            exception.printStackTrace()
        }

    fun fetchForecastDataForCity(city: CityWithForecast) = viewModelScope.launch {
        try {
            forecastRepository.fetchCityForecast(city).collect { result ->
                _singleCityForecast.emit(result)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Resource.Failure(exception)
        }
    }
}