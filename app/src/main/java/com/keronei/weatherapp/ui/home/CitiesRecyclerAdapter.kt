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
package com.keronei.weatherapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keronei.weatherapp.R
import com.keronei.weatherapp.databinding.CityItemBinding
import com.keronei.weatherapp.presentation.CityPresentation
import timber.log.Timber
import java.util.*

class CitiesRecyclerAdapter(
    private val itemSelected: (region: CityPresentation) -> Unit,
    private val context: Context
) :
    ListAdapter<CityPresentation, CitiesRecyclerAdapter.RegionsViewHolder>(FilmDiffUtil()) {

    var untouchedList = listOf<CityPresentation>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionsViewHolder = RegionsViewHolder.from(parent)

    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) {
        val cityPresentation = getItem(position)
        holder.bind(cityPresentation, context)

        holder.binding.root.setOnClickListener {
            itemSelected(cityPresentation)
        }
    }

    fun modifyList(list: List<CityPresentation>) {
        untouchedList = list
        submitList(list)
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<CityPresentation>()

        if (!query.isNullOrEmpty()) {
            list.addAll(
                untouchedList.filter { item ->
                    item.name.lowercase(Locale.getDefault())
                        .contains(query.toString().lowercase(Locale.getDefault())) ||

                        item.country.lowercase(Locale.getDefault())
                            .contains(query.toString().lowercase(Locale.getDefault()))
                }
            )
        } else {
            list.addAll(untouchedList)
        }
        submitList(list)
    }

    class RegionsViewHolder(val binding: CityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cityPresentation: CityPresentation, context: Context) {
            binding.city = cityPresentation

            if (cityPresentation.temperature != null) {
                val formattedTemp = "%.1f".format(cityPresentation.temperature)
                binding.temperature.text = context.getString(R.string.degree_celcius, formattedTemp)
            }

            if (cityPresentation.isFavourite) {
                binding.favourite.visibility = View.VISIBLE
                binding.favourite.setImageDrawable(
                    AppCompatResources.getDrawable(context, R.drawable.ic_baseline_favorite_24)
                )
            } else {
                binding.favourite.visibility = View.GONE
            }

            if (cityPresentation.iconName != "") {
                binding.icon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        context.resources.getIdentifier(
                            cityPresentation.iconName,
                            "drawable",
                            context.packageName
                        )
                    )
                )
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RegionsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemFilmBinding = CityItemBinding.inflate(inflater, parent, false)

                return RegionsViewHolder(itemFilmBinding)
            }
        }
    }

    class FilmDiffUtil : DiffUtil.ItemCallback<CityPresentation>() {
        override fun areItemsTheSame(
            oldItem: CityPresentation,
            newItem: CityPresentation
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CityPresentation,
            newItem: CityPresentation
        ): Boolean = oldItem == newItem
    }
}