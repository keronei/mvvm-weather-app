package com.keronei.weatherapp.ui.viewstate

import com.keronei.weatherapp.data.model.Forecast

sealed class DetailState {
    object Loading : DetailState()
    object Empty : DetailState()
    data class Success(val transaction: Forecast) : DetailState()
    data class Error(val exception: Throwable) : DetailState()
}
