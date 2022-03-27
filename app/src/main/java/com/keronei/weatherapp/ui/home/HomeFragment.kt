package com.keronei.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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

    lateinit var searchView: androidx.appcompat.widget.SearchView

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        searchView = homeFragmentBinding.searchViewAllCities

        return homeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attemptToEstablishCountryAndLoadCities()

        setupCitiesRecycler()

        observeCitiesList()

        implementSearch()
    }

    private fun implementSearch() {
        searchView.setOnCloseListener {
            attemptToEstablishCountryAndLoadCities()
            return@setOnCloseListener false
        }

        // Post query text as it comes.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                citiesRecyclerAdapter.filter(newText)
                return true
            }

        })
    }

    private fun attemptToEstablishCountryAndLoadCities() {
        val country = CountryDeterminerUtil.getCountry(requireContext(), dataStoreManager)
        Timber.d("Init with country as $country")
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
            citiesViewModel.fetchForecastDataForCity(
                cityPresentation.id
            )
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
                    Timber.d("Added ${viewState.citiesPresentations.size} to adapter.")
                    onCitiesListLoaded(viewState.citiesPresentations)
                }
            }
        }
    }

    private fun onCitiesListLoaded(citiesPresentations: List<CityPresentation>) {
        citiesRecyclerAdapter.modifyList(citiesPresentations)
    }
}