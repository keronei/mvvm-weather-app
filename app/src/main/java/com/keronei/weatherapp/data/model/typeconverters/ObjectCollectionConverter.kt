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
package com.keronei.weatherapp.data.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.keronei.weatherapp.data.model.Alert
import com.keronei.weatherapp.data.model.Daily
import com.keronei.weatherapp.data.model.Hourly

class ObjectCollectionConverter {
    private val gsonInstance = Gson()

    @TypeConverter
    fun fromHourlyList(hourly: List<Hourly>): String = gsonInstance.toJson(hourly)

    @TypeConverter
    fun toHourList(json: String?): List<Hourly> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<Hourly>::class.java).asList()
        }
    }

    @TypeConverter
    fun fromDailyList(daily: List<Daily>): String = gsonInstance.toJson(daily)

    @TypeConverter
    fun toDailyList(json: String?): List<Daily> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<Daily>::class.java).asList()
        }
    }

    @TypeConverter
    fun fromAlertsList(alerts: List<Alert>?): String =
        if (alerts == null) "" else gsonInstance.toJson(alerts)

    @TypeConverter
    fun toAlertList(json: String?): List<Alert> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<Alert>::class.java).asList()
        }
    }
}