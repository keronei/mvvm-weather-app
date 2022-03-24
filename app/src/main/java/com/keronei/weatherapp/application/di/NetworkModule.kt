package com.keronei.weatherapp.application.di

import android.content.Context
import com.keronei.weatherapp.BuildConfig
import com.keronei.weatherapp.R
import com.keronei.weatherapp.application.Constants
import com.keronei.weatherapp.data.remote.ApiService
import com.keronei.weatherapp.utils.ConnectivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityProvider =
        ConnectivityProvider(
            context
        )

    @Provides
    @Singleton
    fun interceptWithAppId(@ApplicationContext context: Context): Interceptor {
        return Interceptor { chain ->
            val url = chain.request().url.newBuilder().addQueryParameter(
                "appid", context.resources.getString(
                    R.string.apiKey
                )
            ).build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
    }

    @Singleton
    @Provides
    fun interceptWithLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor(
    ).setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    @Singleton
    @Provides
    fun providesOkHttp3Instance(
        appIdInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(appIdInterceptor)
            .addInterceptor(
                loggingInterceptor
            ).build()

    @Singleton
    @Provides
    fun providesRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}