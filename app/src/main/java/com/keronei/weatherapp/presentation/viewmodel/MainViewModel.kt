package com.keronei.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.ForecastRepository
import com.keronei.weatherapp.domain.mappers.CityObjEntityToCityPresentationWithoutDataMapper
import com.keronei.weatherapp.ui.viewstate.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    private val forecastRepository: ForecastRepository
) :
    ViewModel() {

    private val _cities = MutableStateFlow<ViewState>(value = ViewState.Empty)

    val cities: StateFlow<ViewState> = _cities

    private val first20Cities = mutableListOf<CityWithForecast>()

    fun loadFirstTwentyCitiesFromCountry(country: String) {
        viewModelScope.launch {
            citiesRepository.queryLimitedCitiesCount(
                20,
                country.uppercase(Locale.getDefault())
            ).collect { citiesObject ->
                val citiesAsPresentationWithData =
                    CityObjEntityToCityPresentationWithoutDataMapper().mapList(
                        citiesObject
                    )
                first20Cities.clear()

                first20Cities.addAll(citiesObject)

                _cities.emit(ViewState.Success(citiesAsPresentationWithData))

                fetchCitiesWeatherData()
            }
        }
    }

    private fun fetchCitiesWeatherData() {
        if (first20Cities.isNotEmpty()) {
            viewModelScope.launch {
                first20Cities.forEach { city ->
                    fetchForecastDataForCity(city)
                }
            }
        }
    }

    suspend fun fetchForecastDataForCity(cityPresentation: CityWithForecast) {
        forecastRepository.fetchCityForecast(cityPresentation)
    }


}