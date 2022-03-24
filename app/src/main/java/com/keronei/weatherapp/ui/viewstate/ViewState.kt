package com.keronei.weatherapp.ui.viewstate

import com.keronei.weatherapp.presentation.CityPresentation

sealed class ViewState {
    object Loading : ViewState()
    object Empty : ViewState()
    data class Success(val citiesPresentations: List<CityPresentation>) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
}