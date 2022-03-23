package com.keronei.weatherapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityObjEntity(
    @Embedded
    val coord: Coord,
    val country: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val state: String
)