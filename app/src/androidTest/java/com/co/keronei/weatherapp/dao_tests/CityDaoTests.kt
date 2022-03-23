package com.co.keronei.weatherapp.dao_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keronei.weatherapp.data.local.CityDao
import com.keronei.weatherapp.data.local.WeatherAppDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityDaoTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var weatherAppDatabase: WeatherAppDatabase

    private lateinit var cityDao: CityDao

    @Before
    fun prepareVariables() {
        weatherAppDatabase = DBInstance.initialiseDb()
        cityDao = weatherAppDatabase.cityDao()
    }


    @Test
    fun on_init_complete_the_db_is_populated_with_cities(){
        return runBlocking {
            val expect10Cities = cityDao.queryLimitedCitiesCount(10)

            assertEquals(
                expect10Cities.first().size, 10
            )
        }
    }


}