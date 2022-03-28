/*
 * Copyright 2022 GradleBuildPlugins
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
package com.co.keronei.weatherapp.dao_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keronei.weatherapp.data.local.CityDao
import com.keronei.weatherapp.data.local.WeatherAppDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
    fun on_init_complete_the_db_is_populated_with_cities() {
        return runBlocking {
            val expect10Cities = cityDao.queryLimitedCitiesCount(10, "")

            assertEquals(
                expect10Cities.first().size, 10
            )
        }
    }
}