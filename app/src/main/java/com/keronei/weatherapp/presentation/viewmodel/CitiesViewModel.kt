package com.keronei.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.domain.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(private val citiesRepository: CitiesRepository) :
    ViewModel() {

    private val _cities = MutableStateFlow<List<CityObjEntity>>(value = emptyList())

    val cities: StateFlow<List<CityObjEntity>> = _cities

    init {
        viewModelScope.launch {
            val foundCities = async { citiesRepository.queryLimitedCitiesCount(20) }
            _cities.emit(foundCities.await().first())
        }
    }
}