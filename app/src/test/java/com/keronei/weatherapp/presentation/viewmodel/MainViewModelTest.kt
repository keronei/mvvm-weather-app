package com.keronei.weatherapp.presentation.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keronei.weatherapp.core.Resource
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.data.model.Coord
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.domain.FakeCitiesRepositoryImpl
import com.keronei.weatherapp.domain.FakeForecastRepositoryImpl
import com.keronei.weatherapp.domain.MockData
import com.keronei.weatherapp.ui.viewstate.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var forecast: Forecast

    @Before
    fun setup() {
        mainViewModel = MainViewModel(FakeCitiesRepositoryImpl(), FakeForecastRepositoryImpl())
        forecast = MockData.getForecastResponse()
    }

    @Test
    fun `when loadFirstTwentyCitiesFromCountry is called the cities should not be empty`() {
        mainViewModel.loadFirstTwentyCitiesFromCountry("ke")
        assertThat("View state should not be empty.", mainViewModel.cities.value != ViewState.Empty)
    }

    @Test
    fun `when fetchForecastForCity is called forecast data is returned`() {
        val predefinedForecast = CityWithForecast(
            CityObjEntity(
                Coord(
                    36.0,
                    1.8,
                ),
                "ke", 1, "Kisumu", "", false
            ),
            null
        )

        runBlocking {
            val forecast = mainViewModel.fetchForecastDataForCity(predefinedForecast.cityObjEntity.id)

            forecast.collect { expectedValue ->

                when (expectedValue) {
                    is Resource.Success -> {
                        assertThat("Forecast is preset.", expectedValue.data.equals(forecast))
                    }
                    else -> {
                    }
                }
            }
        }
    }
}