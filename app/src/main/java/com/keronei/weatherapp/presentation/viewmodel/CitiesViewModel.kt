package com.keronei.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.mappers.CityObjEntityToCityPresentationWithoutDataMapper
import com.keronei.weatherapp.ui.viewstate.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(private val citiesRepository: CitiesRepository) :
    ViewModel() {

    private val _cities = MutableStateFlow<ViewState>(value = ViewState.Empty)

    val cities: StateFlow<ViewState> = _cities

    init {
        viewModelScope.launch {
            citiesRepository.queryLimitedCitiesCount(20).collect { citiesObject ->
                val citiesAsPresentationWithoutData =
                    CityObjEntityToCityPresentationWithoutDataMapper().mapList(
                        citiesObject
                    )
                _cities.emit(ViewState.Success(citiesAsPresentationWithoutData))
            }

        }
    }
}