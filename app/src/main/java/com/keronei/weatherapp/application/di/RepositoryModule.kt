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
package com.keronei.weatherapp.application.di

import com.keronei.weatherapp.data.local.CityDao
import com.keronei.weatherapp.data.local.ForecastDao
import com.keronei.weatherapp.data.remote.NetworkDataSource
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.CitiesRepositoryImpl
import com.keronei.weatherapp.domain.ForecastRepository
import com.keronei.weatherapp.domain.ForecastRepositoryImpl
import com.keronei.weatherapp.utils.ConnectivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesCitiesRepository(cityDao: CityDao): CitiesRepository = CitiesRepositoryImpl(cityDao)

    @Provides
    @Singleton
    fun providesForecastRepository(
        networkDataSource: NetworkDataSource,
        forecastDao: ForecastDao,
        connectivityProvider: ConnectivityProvider
    ): ForecastRepository =
        ForecastRepositoryImpl(networkDataSource, forecastDao, connectivityProvider)
}