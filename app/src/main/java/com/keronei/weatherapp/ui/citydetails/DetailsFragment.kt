package com.keronei.weatherapp.ui.citydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.keronei.weatherapp.R
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.databinding.DetailsFragmentBinding
import com.keronei.weatherapp.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val citiesViewModel: MainViewModel by activityViewModels()
    private lateinit var detailBinding: DetailsFragmentBinding
    private var selectedForecast: CityWithForecast? = null
    lateinit var recyclerAdapter: DailyForecastRecyclerAdapter

    val detailsViewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.details_fragment, container, false)
        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateDisplayDetail()
        prepareRecycler()
        listenToClicks()
    }

    private fun listenToClicks() {
        var favStatus = selectedForecast!!.cityObjEntity.favourite

        detailBinding.btnFavourite.setOnClickListener {
            favStatus = !favStatus

            if (selectedForecast?.cityObjEntity != null) {
                detailsViewModel.toggleFavourite(selectedForecast!!.cityObjEntity)

                if (favStatus) {
                    displayFavIcon()
                } else {
                    displayUnfavouriteIcon()
                }

            } else {
                Toast.makeText(requireContext(), "Couldn't switch this.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateDisplayDetail() {
        selectedForecast = citiesViewModel.selectedCity
        val city = selectedForecast!!.cityObjEntity
        val forecast = selectedForecast?.forecast

        detailBinding.cityName.text = city.city_ascii
        detailBinding.country.text = city.country

        if (city.favourite) {
            displayFavIcon()
        } else {
            displayUnfavouriteIcon()
        }

        if (forecast != null) {
            val calendar = Calendar.getInstance()
            val latestInfoTimestamp =
                (forecast.daily.maxByOrNull { day -> day.dt }?.dt ?: 0) * 1000L

            val currentTimestamp = calendar.timeInMillis
            detailBinding.outdatedDataStatus.visibility =
                if (latestInfoTimestamp > currentTimestamp) View.VISIBLE else View.GONE

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val todayData =
                forecast.daily.firstOrNull { day -> day.dt * 1000L == calendar.timeInMillis }

            val prominentIconString = todayData?.weather?.first()?.icon
            val displayIcon = if (prominentIconString != null) "a$prominentIconString" else null

            detailBinding.todayIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    requireContext().resources.getIdentifier(
                        displayIcon ?: "a50d",
                        "drawable",
                        requireContext().packageName
                    )
                )
            )

            recyclerAdapter.submitList(forecast.daily)
        } else {
            detailBinding.outdatedDataStatus.visibility = View.VISIBLE
            detailBinding.outdatedDataStatus.text = getString(R.string.no_data)
        }
    }

    private fun displayUnfavouriteIcon() {
        detailBinding.btnFavourite.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_favorite_border_24
            )
        )
    }

    private fun displayFavIcon() {
        detailBinding.btnFavourite.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_favorite_24
            )
        )
    }

    private fun prepareRecycler() {
        recyclerAdapter = DailyForecastRecyclerAdapter(requireContext())
        detailBinding.sevenDayWeatherRecycler.adapter = recyclerAdapter
    }

}