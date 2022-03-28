package com.keronei.weatherapp.ui.citydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.keronei.weatherapp.R
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.databinding.DetailsFragmentBinding
import com.keronei.weatherapp.presentation.viewmodel.MainViewModel
import com.keronei.weatherapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val citiesViewModel: MainViewModel by activityViewModels()
    private lateinit var detailBinding: DetailsFragmentBinding
    private var selectedForecast: CityWithForecast? = null
    lateinit var recyclerAdapter: DailyForecastRecyclerAdapter

    private val detailsViewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.details_fragment, container, false)
        prepareRecycler()
        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateDisplayDetail()
        listenToClicks()
    }

    private fun listenToClicks() {
        var favStatus = selectedForecast!!.cityObjEntity.favourite

        detailBinding.btnFavourite.setOnClickListener {
            favStatus = !favStatus

            if (selectedForecast?.cityObjEntity != null) {
                detailsViewModel.toggleFavourite(selectedForecast!!.cityObjEntity)
                updateFavouriteButtonIcon(favStatus)
            } else {
                showToast(getString(R.string.unable_to_switch_fav))
            }
        }
    }

    private fun populateDisplayDetail() {
        selectedForecast = citiesViewModel.selectedCity
        val city = selectedForecast!!.cityObjEntity
        val forecast = selectedForecast?.forecast

        detailBinding.cityName.text = city.city_ascii
        detailBinding.country.text = city.country

        updateFavouriteButtonIcon(city.favourite)

        if (forecast != null) {
            val calendar = Calendar.getInstance()
            val latestInfoTimestamp =
                (forecast.daily.maxByOrNull { day -> day.dt }?.dt ?: 0).fromUnixTimestamp()

            val currentTimestamp = calendar.timeInMillis
            detailBinding.outdatedDataStatus.hideIf { latestInfoTimestamp > currentTimestamp }

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val todayData =
                forecast.daily.firstOrNull { day -> day.dt.fromUnixTimestamp() == calendar.timeInMillis }

            val prominentIconString = todayData?.weather?.first()?.icon
            val displayIcon = if (prominentIconString != null) "a$prominentIconString" else null

            detailBinding.todayIcon.setImageDrawable(
                getDrawableWithName(requireContext(), displayIcon ?: "a50d")
            )

            recyclerAdapter.submitList(forecast.daily)
        } else {
            detailBinding.outdatedDataStatus.show()
            detailBinding.outdatedDataStatus.text = getString(R.string.no_data)
        }
    }

    private fun updateFavouriteButtonIcon(status: Boolean) {
        detailBinding.btnFavourite.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                if (status) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            )
        )
    }

    private fun prepareRecycler() {
        recyclerAdapter = DailyForecastRecyclerAdapter(requireContext())
        detailBinding.sevenDayWeatherRecycler.adapter = recyclerAdapter
    }

}