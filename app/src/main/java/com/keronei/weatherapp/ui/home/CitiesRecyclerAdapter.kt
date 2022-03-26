package com.keronei.weatherapp.ui.home

import android.content.Context
import android.view.LayoutInflater
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
        val region = getItem(position)
        holder.bind(region, context)

        holder.binding.root.setOnClickListener {
            itemSelected(region)
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