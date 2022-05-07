/*
 * Copyright 2022 Keronei Lincoln
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
package com.keronei.weatherapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityObjEntity(
    val lat: Double,
    val lng: Double,
    @ColumnInfo(defaultValue = "")
    val iso2: String,
    val country: String,
    @ColumnInfo(index = true)
    @PrimaryKey(autoGenerate = true)
    val identity: Int,
    val city_ascii: String,
    val city: String,
    var favourite: Boolean = false
)