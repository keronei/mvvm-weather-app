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
package com.keronei.weatherapp.application.di

import android.content.Context
import com.keronei.weatherapp.data.local.CityDao
import com.keronei.weatherapp.data.local.ForecastDao
import com.keronei.weatherapp.data.local.WeatherAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideDatabaseInstance(
        @ApplicationContext context: Context
    ): WeatherAppDatabase = WeatherAppDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun providesCitiesDao(weatherAppDatabase: WeatherAppDatabase): CityDao =
        weatherAppDatabase.cityDao()

    @Provides
    @Singleton
    fun providesForecastDao(weatherAppDatabase: WeatherAppDatabase): ForecastDao =
        weatherAppDatabase.forecastDao()
}