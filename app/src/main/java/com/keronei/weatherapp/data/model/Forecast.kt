package com.keronei.weatherapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Forecast (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Embedded
    val alerts: List<Alert>,
    @Embedded
    val current: Current,
    @Embedded
    val daily: List<Daily>,
    @Embedded
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    @Embedded
    val minutely: List<Minutely>,
    val timezone: String,
    val timezone_offset: Int
)