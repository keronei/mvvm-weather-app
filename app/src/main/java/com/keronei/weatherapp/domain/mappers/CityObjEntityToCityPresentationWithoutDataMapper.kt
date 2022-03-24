package com.keronei.weatherapp.domain.mappers

import com.keronei.weatherapp.core.mapper.Mapper
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.presentation.CityPresentation
import okhttp3.internal.format
import java.math.RoundingMode

class CityObjEntityToCityPresentationWithoutDataMapper : Mapper<CityObjEntity, CityPresentation>() {
    override fun map(input: CityObjEntity): CityPresentation =
        CityPresentation(
            input.id,
            input.name,
            format("%.4f", input.coord.lat).toDouble(),
            format("%.4f", input.coord.lon).toDouble(),
            input.country,
            "-",
            ""
        )
}