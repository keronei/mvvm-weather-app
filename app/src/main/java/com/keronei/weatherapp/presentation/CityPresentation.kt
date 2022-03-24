package com.keronei.weatherapp.presentation

data class CityPresentation(
    val id: Int,
    val name: String,
    val country : String,
    val temperature: String,
    val iconName: String
)