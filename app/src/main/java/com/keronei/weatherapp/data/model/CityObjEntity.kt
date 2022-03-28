package com.keronei.weatherapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
data class CityObjEntity(
    val lat: Double,
    val lng : Double,
    @ColumnInfo(defaultValue = "")
    val iso2: String,
    val country : String,
    @ColumnInfo(index = true)
    @PrimaryKey(autoGenerate = true)
    val identity: Int,
    val city_ascii: String,
    val city: String,
    var favourite: Boolean = false
)