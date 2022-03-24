package com.keronei.weatherapp.presentation

data class CityPresentation(
    val id: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val temperature: String,
    val iconName: String
)