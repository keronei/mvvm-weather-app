package com.keronei.weatherapp.domain.mappers

import com.keronei.weatherapp.core.mapper.Mapper
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.presentation.CityPresentation
import okhttp3.internal.format
import timber.log.Timber
import java.util.*

class CityObjEntityToCityPresentationWithoutDataMapper :
    Mapper<CityWithForecast, CityPresentation>() {
    override fun map(input: CityWithForecast): CityPresentation {
        val currentTime = Calendar.getInstance()
        var temperature: Double? = null
        var iconName = ""

        currentTime.set(Calendar.MINUTE, 0)
        currentTime.set(Calendar.SECOND, 0)
        currentTime.set(Calendar.MILLISECOND, 0)
        val startHour = currentTime.timeInMillis

        if (input.forecast != null) {
            val fc = input.forecast
            val hourUpdate = fc.hourly.firstOrNull { hourly -> hourly.dt * 1000L == startHour }

            if (hourUpdate != null) {
                temperature = (hourUpdate.temp - 273.15) // to Celsius
                val rawIc = hourUpdate.weather.firstOrNull()?.icon
                iconName = if (rawIc != null) {
                    "a$rawIc"
                } else {
                    ""
                }
            } else {
                Timber.d("hourUpdate null in mapper")
            }
        }

        return CityPresentation(
            input.cityObjEntity.identity,
            input.cityObjEntity.city_ascii,
            format("%.4f", input.cityObjEntity.lat).toDouble(),
            format("%.4f", input.cityObjEntity.lng).toDouble(),
            input.cityObjEntity.favourite,
            input.cityObjEntity.country,
            temperature,
            iconName,
            input.forecast

        )
    }
}