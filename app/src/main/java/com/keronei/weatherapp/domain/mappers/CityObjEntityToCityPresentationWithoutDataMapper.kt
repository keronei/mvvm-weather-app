package com.keronei.weatherapp.domain.mappers

import com.keronei.weatherapp.core.mapper.Mapper
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.presentation.CityPresentation

class CityObjEntityToCityPresentationWithoutDataMapper : Mapper<CityObjEntity, CityPresentation>() {
    override fun map(input: CityObjEntity): CityPresentation = CityPresentation(input.id, input.name, "-", "")
}