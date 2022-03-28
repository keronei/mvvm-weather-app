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
package com.keronei.weatherapp.ui.citydetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keronei.weatherapp.R
import com.keronei.weatherapp.data.model.Daily
import com.keronei.weatherapp.databinding.DailyForecastBinding
import com.keronei.weatherapp.utils.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastRecyclerAdapter(
    private val context: Context
) :
    ListAdapter<Daily, DailyForecastRecyclerAdapter.RegionsViewHolder>(FilmDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionsViewHolder = RegionsViewHolder.from(parent)

    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) {
        val daily = getItem(position)
        holder.bind(daily, context)
    }

    class RegionsViewHolder(private val binding: DailyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: Daily, context: Context) {
            val parser = SimpleDateFormat("EEE, dd MMM", Locale.US)

            binding.dayDate.text = parser.format(Date(day.dt.fromUnixTimestamp()))

            binding.dayMaxTemperature.text = day.temp.max.toCelsius().trimDecimalThenToString(context)

            binding.dayMinTemperature.text = day.temp.min.toCelsius().trimDecimalThenToString(context)

            if (day.weather.isNotEmpty()) {
                binding.dayProminentIcon.setImageDrawable(
                    getDrawableWithName(context, "a${day.weather.first().icon}")
                )

                binding.dayWeatherDescription.text =
                    day.weather.first().description.capitaliseFirstCharacter()
            }
        }

        companion object {
            fun from(parent: ViewGroup): RegionsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemFilmBinding = DailyForecastBinding.inflate(inflater, parent, false)
                return RegionsViewHolder(itemFilmBinding)
            }
        }
    }

    class FilmDiffUtil : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(
            oldItem: Daily,
            newItem: Daily
        ): Boolean = oldItem.dt == newItem.dt

        override fun areContentsTheSame(
            oldItem: Daily,
            newItem: Daily
        ): Boolean = oldItem == newItem
    }
}