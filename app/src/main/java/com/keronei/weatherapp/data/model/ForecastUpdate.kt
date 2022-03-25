package com.keronei.weatherapp.data.model

data class ForecastUpdate(
    val cityId: Int,
    val alerts: List<Alert>,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
)
