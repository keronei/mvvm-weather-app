package com.keronei.weatherapp.core.worker


import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.keronei.weatherapp.application.Constants.NOTIFICATION_CHANNEL_ID
import com.keronei.weatherapp.data.model.CityWithForecast
import com.keronei.weatherapp.domain.CitiesRepository
import com.keronei.weatherapp.utils.fromUnixTimestamp
import com.keronei.weatherapp.utils.toCelsius
import com.keronei.weatherapp.utils.trimDecimalThenToString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val citiesRepository: CitiesRepository,
    private val coroutineScope: CoroutineScope
) : Worker(context, params) {
    override fun doWork(): Result {
        /**
         * Query all favourite cities
         * Check if there's data for current hour
         * Check is there's alerts to be displayed.
         */

        coroutineScope.launch {
            citiesRepository.queryFavouritedCities().collect { favourited ->
                /**
                 * Get all favourited AND with data
                 */
                val favoritedCities =
                    favourited.filter { cityWithForecast -> cityWithForecast.forecast != null }

                if (favoritedCities.isNotEmpty()) {
                    val citiesForNotification =
                        favoritedCities.filter { city -> city.forecast!!.hourly.all { hourly -> hourly.dt.fromUnixTimestamp() == getCurrentHour().timeInMillis } }

                    citiesForNotification.forEach { city ->
                        displayNotification(applicationContext, city)
                    }
                }
            }
        }


        /**
         * The assumption is that the notification would be handled by the system.
         */
        return Result.success()
    }

    private fun displayNotification(context: Context, cityWithForecast: CityWithForecast) {

        // Get the particular hour
        val hour =
            cityWithForecast.forecast!!.hourly.first { hourly -> hourly.dt.fromUnixTimestamp() == getCurrentHour().timeInMillis }

        val header = cityWithForecast.cityObjEntity.city_ascii + " hourly update"
        val temperatureAsStringMessage =
            "Currently at ${hour.temp.toCelsius().trimDecimalThenToString(context)}"

        // Parse the icon
        val iconId = context.resources.getIdentifier(
            "a${hour.weather.first().icon}",
            "drawable",
            context.packageName
        )
        val cityId = cityWithForecast.cityObjEntity.identity

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(iconId)
            .setContentTitle(header)
            .setContentText(temperatureAsStringMessage)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(cityId, builder.build())
        }

        // Display the notification
        builder.build()
    }

    private fun getCurrentHour(): Calendar {
        val currentHour = Calendar.getInstance()
        currentHour.set(Calendar.MILLISECOND, 0)
        currentHour.set(Calendar.SECOND, 0)
        currentHour.set(Calendar.MINUTE, 0)
        return currentHour
    }

}