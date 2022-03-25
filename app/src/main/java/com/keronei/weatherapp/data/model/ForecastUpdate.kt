package com.keronei.weatherapp.data.model

data class ForecastUpdate(
    val id : Int,
    val alerts: List<Alert>,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
)
