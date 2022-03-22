package com.keronei.weatherapp.data.remote

import retrofit2.http.GET

interface ApiService {
    @GET()
    suspend fun getCityWeatherForeCast()
}