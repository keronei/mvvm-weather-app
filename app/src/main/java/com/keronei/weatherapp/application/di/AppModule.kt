package com.keronei.weatherapp.application.di

import android.content.Context
import com.keronei.weatherapp.application.preference.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataStoreInstance(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)

}