package com.keronei.weatherapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CityWithForecast(
    @Embedded val cityObjEntity: CityObjEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cityId"
    )
    val forecast: Forecast?
)