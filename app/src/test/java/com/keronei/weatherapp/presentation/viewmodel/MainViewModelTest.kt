/*
 * Copyright 2022 Keronei Lincoln
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
package com.keronei.weatherapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.domain.FakeCitiesRepositoryImpl
import com.keronei.weatherapp.domain.FakeForecastRepositoryImpl
import com.keronei.weatherapp.domain.MockData
import com.keronei.weatherapp.ui.viewstate.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var forecast: Forecast

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(
            FakeCitiesRepositoryImpl(),
            FakeForecastRepositoryImpl()
        )
        forecast = MockData.getForecastResponse()
    }

    @Test
    fun `when loadFirstTwentyCitiesFromCountry is called the cities should not be empty`() = runBlocking {

        mainViewModel.loadFirstTwentyCitiesFromCountry("ke")

        val viewStateWithCity = mainViewModel.cities.drop(1).first()

        assertThat("View state should not be empty.", viewStateWithCity != ViewState.Empty)
    }

    @Test
    fun `when fetchForecastForCity is called forecast data is returned`() = runBlocking {
        var returnedForecastObject: Forecast? = null

        val predefinedForecast = CityWithForecast(
            CityObjEntity(

                36.0,
                1.8,

                "ke", "", 1, "Kisumu", "", false
            ),
            MockData.getForecastResponse()
        )

        val forecast =
            mainViewModel.fetchForecastDataForCity(predefinedForecast)

        when (val actualForecastData = forecast.drop(1).first()) {
            is Resource.Success -> {
                returnedForecastObject = actualForecastData.data
            }

            else -> {
                // Was not successful.
            }
        }

        assertThat(
            "Fetch forecast should return forecast data",
            returnedForecastObject == predefinedForecast.forecast
        )
    }
}