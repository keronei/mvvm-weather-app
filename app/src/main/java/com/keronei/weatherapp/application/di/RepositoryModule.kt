package com.keronei.weatherapp.application.di

import com.keronei.weatherapp.data.local.CityDao
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.domain.CitiesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesCitiesRepository(cityDao: CityDao): CitiesRepository {
        return CitiesRepositoryImpl(cityDao)
    }
}