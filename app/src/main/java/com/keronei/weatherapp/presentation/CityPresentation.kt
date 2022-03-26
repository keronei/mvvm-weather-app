package com.keronei.weatherapp.presentation

import com.keronei.weatherapp.data.model.Forecast

data class CityPresentation(
    val id: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val temperature: Double?,
    val iconName: String,
    val forecast: Forecast?
)