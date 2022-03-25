package com.keronei.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.keronei.weatherapp.R
import com.keronei.weatherapp.application.preference.DataStoreManager
import com.keronei.weatherapp.databinding.HomeFragmentBinding
import com.keronei.weatherapp.presentation.CityPresentation
import com.keronei.weatherapp.presentation.viewmodel.MainViewModel
import com.keronei.weatherapp.ui.viewstate.ViewState
import com.keronei.weatherapp.utils.CountryDeterminerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val citiesViewModel: MainViewModel by activityViewModels()

    private lateinit var citiesRecyclerAdapter: CitiesRecyclerAdapter

    private lateinit var homeFragmentBinding: HomeFragmentBinding

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        return homeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attemptToEstablishCountry()

        setupCitiesRecycler()

        observeCitiesList()
    }

    private fun attemptToEstablishCountry() {
        val country = CountryDeterminerUtil.getCountry(requireContext(), dataStoreManager)
        citiesViewModel.loadFirstTwentyCitiesFromCountry(country ?: "")
    }

    private fun setupCitiesRecycler() {
        citiesRecyclerAdapter = CitiesRecyclerAdapter(::citySelected, requireContext())
        homeFragmentBinding.recyclerCities.apply {
            adapter = citiesRecyclerAdapter
        }
    }

    private fun citySelected(cityPresentation: CityPresentation) {
        lifecycleScope.launch {
            citiesViewModel.fetchForecastDataForCity(cityPresentation.id)
        }
        Toast.makeText(context, cityPresentation.name, Toast.LENGTH_SHORT).show()
    }

    private fun observeCitiesList() = lifecycleScope.launchWhenStarted {
        citiesViewModel.cities.collect { viewState ->

            when (viewState) {
                ViewState.Empty -> {

                }
                is ViewState.Error -> {

                }
                ViewState.Loading -> {

                }
                is ViewState.Success -> {
                    onCitiesListLoaded(viewState.citiesPresentations)
                }
            }
        }
    }

    private fun onCitiesListLoaded(citiesPresentations: List<CityPresentation>) {
        citiesRecyclerAdapter.submitList(citiesPresentations)
    }
}