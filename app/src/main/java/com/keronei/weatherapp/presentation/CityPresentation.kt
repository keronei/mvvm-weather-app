/*
 * Copyright 2022 GradleBuildPlugins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.weatherapp.presentation

import com.keronei.weatherapp.data.model.Forecast

data class CityPresentation(
    val id: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val isFavourite: Boolean,
    val country: String,
    val temperature: Double?,
    val iconName: String,
    val forecast: Forecast?
)