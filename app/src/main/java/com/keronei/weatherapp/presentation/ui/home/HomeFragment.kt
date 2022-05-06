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
package com.keronei.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keronei.weatherapp.R
import com.keronei.weatherapp.application.preference.DataStoreManager
import com.keronei.weatherapp.databinding.HomeFragmentBinding
import com.keronei.weatherapp.presentation.CityPresentation
import com.keronei.weatherapp.presentation.viewmodel.MainViewModel
import com.keronei.weatherapp.ui.viewstate.ViewState
import com.keronei.weatherapp.utils.CountryDeterminerUtil
import com.keronei.weatherapp.utils.onQueryTextChanged
import com.keronei.weatherapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val citiesViewModel: MainViewModel by activityViewModels()

    private lateinit var citiesRecyclerAdapter: CitiesRecyclerAdapter

    private lateinit var homeFragmentBinding: HomeFragmentBinding

    lateinit var searchView: SearchView

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

        refreshList()
    }

    private fun refreshList() {
        homeFragmentBinding.swipeToRefresh.setOnRefreshListener {
            citiesViewModel.refreshData()
            homeFragmentBinding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun implementSearch() {
        searchView.onQueryTextChanged { searchQuery ->
            citiesRecyclerAdapter.filter(searchQuery)
        }
    }

    private fun attemptToEstablishCountryAndLoadCities() {
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
            citiesViewModel.fetchForCityWithId(
                cityPresentation.id
            )
        }

        citiesViewModel.setSelectedCity(cityPresentation)

        if (citiesViewModel.selectedCity != null) {
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
        } else {
            showToast(getString(R.string.no_detail))
        }
    }

    private fun observeCitiesList() = lifecycleScope.launchWhenStarted {
        citiesViewModel.cities.collect { viewState ->

            when (viewState) {
                ViewState.Empty -> {
                    // Data is prefilled, only empty when searching.
                    homeFragmentBinding.loadingIndicator.hide()
                }
                is ViewState.Error -> {
                    homeFragmentBinding.loadingIndicator.hide()
                }
                ViewState.Loading -> {
                    homeFragmentBinding.loadingIndicator.show()
                }
                is ViewState.Success -> {
                    homeFragmentBinding.loadingIndicator.hide()
                    onCitiesListLoaded(viewState.citiesPresentations)
                    homeFragmentBinding.swipeToRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun onCitiesListLoaded(citiesPresentations: List<CityPresentation>) {
        citiesRecyclerAdapter.modifyList(citiesPresentations)
    }
}