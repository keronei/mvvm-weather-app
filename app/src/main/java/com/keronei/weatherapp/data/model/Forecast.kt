package com.keronei.weatherapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = CityObjEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cityId"),
        onDelete = CASCADE
    )]
)
data class Forecast(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var cityId: Int,
    val alerts: List<Alert>,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)