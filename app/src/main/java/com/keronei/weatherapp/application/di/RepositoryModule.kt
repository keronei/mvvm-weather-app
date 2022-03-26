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