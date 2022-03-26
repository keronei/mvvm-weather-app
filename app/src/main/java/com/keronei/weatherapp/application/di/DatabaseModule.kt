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
    ) : WeatherAppDatabase = WeatherAppDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun providesCitiesDao(weatherAppDatabase: WeatherAppDatabase): CityDao =
        weatherAppDatabase.cityDao()

    @Provides
    @Singleton
    fun providesForecastDao(weatherAppDatabase: WeatherAppDatabase): ForecastDao =
        weatherAppDatabase.forecastDao()
}