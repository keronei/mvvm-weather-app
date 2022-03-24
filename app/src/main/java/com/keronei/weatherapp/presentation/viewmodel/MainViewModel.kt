package com.keronei.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.ForecastRepository
import com.keronei.weatherapp.domain.mappers.CityObjEntityToCityPresentationWithoutDataMapper
import com.keronei.weatherapp.presentation.CityPresentation
import com.keronei.weatherapp.ui.viewstate.ViewState
import com.keronei.weatherapp.utils.ConnectivityProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
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

    private val first20Cities = mutableListOf<CityPresentation>()

    init {
        if (first20Cities.isNotEmpty()) {
            viewModelScope.launch {
                first20Cities.forEach { city ->
                    fetchForecast(city)
                }
            }
        }
    }

    fun loadFirstTwentyCitiesFromCountry(country: String) {
        viewModelScope.launch {
            citiesRepository.queryLimitedCitiesCount(
                20,
                country.uppercase(Locale.getDefault())
            ).collect { citiesObject ->
                val citiesAsPresentationWithoutData =
                    CityObjEntityToCityPresentationWithoutDataMapper().mapList(
                        citiesObject
                    )
                first20Cities.clear()

                first20Cities.addAll(citiesAsPresentationWithoutData)

                _cities.emit(ViewState.Success(citiesAsPresentationWithoutData))
            }
        }
    }

    fun fetchForecastDataForCity(cityPresentation: CityPresentation) {
        viewModelScope.launch {
            val forecast = fetchForecast(cityPresentation)
        }
    }


    private suspend fun fetchForecast(cityPresentation: CityPresentation) =
        forecastRepository.fetchCityForecast(cityPresentation.lat, cityPresentation.lon)


}