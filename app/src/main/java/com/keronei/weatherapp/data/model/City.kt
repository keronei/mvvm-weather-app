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
data class CityEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val lon: Double,
    val lat: Double,
    val country: String,
    val lastUpdateTimeStamp: Long,
    val isFavourite: Boolean
)

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val lon: Double,
    val lat: Double,
    val country: String,
    val isFavourite: Boolean
) : Parcelable