package com.keronei.weatherapp.data.model

import androidx.room.Entity
import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/***
 * Working with provided data, actual object from json has the following data
 * {"id": 833, "name": "Ḩeşār-e Sefīd", "state": "", "country": "IR", "coord": {"lon": 47.159401, "lat": 34.330502}}
 */

@Entity
data class CityEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val lon: Double,
    val lat: Double,
    val country: String,
    val lastUpdateTimeStamp : Long,
    val isFavourite: Boolean
)


@Parcelize
data class City (
    val id: Int,
    val name: String,
    val lon: Double,
    val lat: Double,
    val country: String,
    val isFavourite: Boolean
) : Parcelable
