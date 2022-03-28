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
package com.keronei.weatherapp.application

object Constants {
    // Network endpoints constants
    const val BASE_URL = "https://api.openweathermap.org"

    const val ONE_CALL_FORECAST_ENDPOINT = "/data/2.5/onecall"

    const val FIRST_COUNT = 20

    const val KELVIN_CONVERSION = 273.15

    // String constants
    const val COUNTRY = "user_country"

    const val DATABASE_NAME = "weatherappdatabase.db"

    const val DEFAULT_FIELDS_TO_EXCLUDE = "minutely,current"

    const val NOTIFICATION_CHANNEL_ID = "channel_1"

    const val TEMPERATURE_UPDATE_MESSAGE = "update"

    const val HEADER = "heading"

    const val ICON_ID = "icon"

    const val CITY_ID = "cityId"

    const val NOTIFICATION_MANAGER_TAG = "notify-city-tag"
}