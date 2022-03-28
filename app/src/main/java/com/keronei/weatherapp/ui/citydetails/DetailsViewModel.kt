package com.keronei.weatherapp.ui.citydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.domain.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val citiesRepository: CitiesRepository) :
    ViewModel() {
    fun toggleFavourite(cityObjEntity: CityObjEntity) {
        cityObjEntity.favourite = !cityObjEntity.favourite
        viewModelScope.launch {
            citiesRepository.toggleFavourite(cityObjEntity)
        }
    }
}