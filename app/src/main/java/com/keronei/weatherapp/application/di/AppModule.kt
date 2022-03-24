package com.keronei.weatherapp.application.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.keronei.weatherapp.application.Constants.BASE_URL
import com.keronei.weatherapp.application.preference.DataStoreManager
import com.keronei.weatherapp.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataStoreInstance(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)

    @Singleton
    @Provides
    fun providesOkHttp3Instance(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun providesRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().create())
        ).build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}