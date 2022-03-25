package com.keronei.weatherapp.domain.mappers

import com.keronei.weatherapp.core.mapper.Mapper
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.presentation.CityPresentation
import okhttp3.internal.format
import java.math.RoundingMode

class CityObjEntityToCityPresentationWithoutDataMapper : Mapper<CityWithForecast, CityPresentation>() {
    override fun map(input: CityWithForecast): CityPresentation =
        CityPresentation(
            input.cityObjEntity.id,
            input.cityObjEntity.name,
            format("%.4f", input.cityObjEntity.coord.lat).toDouble(),
            format("%.4f", input.cityObjEntity.coord.lon).toDouble(),
            input.cityObjEntity.country,
            "-",
            "",
            input.forecast

        )
}