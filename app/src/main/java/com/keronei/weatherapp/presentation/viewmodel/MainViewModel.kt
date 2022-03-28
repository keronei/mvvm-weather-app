package com.keronei.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.ForecastRepository
import com.keronei.weatherapp.domain.mappers.CityObjEntityToCityPresentationWithoutDataMapper
import com.keronei.weatherapp.presentation.CityPresentation
import com.keronei.weatherapp.ui.viewstate.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    private val forecastRepository: ForecastRepository,
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
                20,
                country.uppercase(Locale.getDefault())
            ).collect { citiesObject ->

                val citiesAsPresentationWithData =
                    CityObjEntityToCityPresentationWithoutDataMapper().mapList(
                        citiesObject, viewModelScope
                    )
                first20Cities.clear()

                first20Cities.addAll(citiesObject)
                Timber.d("Emitting from first 20 ${citiesAsPresentationWithData.size}")

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
            } catch (exception: Exception) {
                if (exception is CancellationException) {
                    throw exception
                }
                exception.printStackTrace()
            }
        }
    }
}