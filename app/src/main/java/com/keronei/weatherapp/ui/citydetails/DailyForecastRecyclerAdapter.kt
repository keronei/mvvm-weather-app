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
import com.keronei.weatherapp.utils.capitaliseFirstCharacter
import com.keronei.weatherapp.utils.getDrawableWithName
import com.keronei.weatherapp.utils.toCelsius
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
            val dayMaxTemptInString =
                context.getString(R.string.format_to_one_dp).format(day.temp.max.toCelsius())
            binding.dayMaxTemperature.text =
                context.getString(R.string.degree_celcius, dayMaxTemptInString)
            val dayMinTempInString =
                context.getString(R.string.format_to_one_dp).format(day.temp.min.toCelsius())
            binding.dayMinTemperature.text =
                context.getString(
                    R.string.degree_celcius,
                    dayMinTempInString
                )

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