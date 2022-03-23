package com.co.keronei.weatherapp.dao_tests

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.keronei.weatherapp.data.local.WeatherAppDatabase

object DBInstance {
    lateinit var weatherAppDatabase: WeatherAppDatabase

    fun initialiseDb(): WeatherAppDatabase {
        weatherAppDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            WeatherAppDatabase::class.java
        ).build()

        return weatherAppDatabase
    }
}