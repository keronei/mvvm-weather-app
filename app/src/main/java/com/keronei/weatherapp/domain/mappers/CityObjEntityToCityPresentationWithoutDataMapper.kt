package com.keronei.weatherapp.domain.mappers

import com.keronei.weatherapp.core.mapper.Mapper
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.presentation.CityPresentation
import okhttp3.internal.format
import timber.log.Timber
import java.math.RoundingMode
import java.util.*

class CityObjEntityToCityPresentationWithoutDataMapper :
    Mapper<CityWithForecast, CityPresentation>() {
    override fun map(input: CityWithForecast): CityPresentation {
        val currentTime = Calendar.getInstance()
        var temperature = "-"
        var iconName = ""

        val hour = currentTime.get(Calendar.HOUR)
        val minutes = currentTime.get(Calendar.MINUTE)

        //currentTime.set(Calendar.HOUR, hour + 1)
        currentTime.set(Calendar.MINUTE, 0)

        val minutesToTopOfHour = 60 - minutes

        val startHour = currentTime.time.time

        currentTime.set(Calendar.MINUTE, 60)

        if (input.forecast != null) {
            val fc = input.forecast
            val hourUpdate =
                fc.hourly.firstOrNull { hourly -> (hourly.dt * 1000) in startHour..currentTime.time.time }

            Timber.d(
                "provided ${hourUpdate?.dt.toString()} Using start of ${
                    Date(
                        startHour
                    )
                } and end of ${currentTime.time}"
            )

            if (hourUpdate != null) {
                temperature = hourUpdate.temp.toString()
                val rawIc = hourUpdate.weather.firstOrNull()?.icon
                iconName = if (rawIc != null) {
                    "a$rawIc"
                } else {
                    ""
                }
            } else {
                Timber.d("hourUpdate null in mapper")
            }
        } else {
            Timber.d("Forecast Null in mapper")
        }

        return CityPresentation(
            input.cityObjEntity.id,
            input.cityObjEntity.name,
            format("%.4f", input.cityObjEntity.coord.lat).toDouble(),
            format("%.4f", input.cityObjEntity.coord.lon).toDouble(),
            input.cityObjEntity.country,
            temperature,
            iconName,
            input.forecast

        )
    }
}