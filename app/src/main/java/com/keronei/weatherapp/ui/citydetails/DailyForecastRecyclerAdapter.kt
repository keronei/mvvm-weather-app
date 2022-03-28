package com.keronei.weatherapp.ui.citydetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keronei.weatherapp.R
import com.keronei.weatherapp.data.model.Daily
import com.keronei.weatherapp.databinding.DailyForecastBinding
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
            val parser = SimpleDateFormat("dd MMM", Locale.US)

            binding.dayDate.text = parser.format(Date(day.dt * 1000L))
            binding.dayMaxTemperature.text =
                context.getString(R.string.format_to_one_dp).format(day.temp.max - 273.15)
            binding.dayMinTemperature.text =
                context.getString(R.string.format_to_one_dp).format(day.temp.min - 273.15)

            if (day.weather.isNotEmpty()) {
                binding.dayProminentIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        context.resources.getIdentifier(
                            "a${day.weather.first().icon}",
                            "drawable",
                            context.packageName
                        )
                    )
                )

                binding.dayWeatherDescription.text =
                    day.weather.first().description.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }
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