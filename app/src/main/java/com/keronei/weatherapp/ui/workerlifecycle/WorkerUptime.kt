package com.keronei.weatherapp.ui.workerlifecycle

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.keronei.weatherapp.application.Constants
import com.keronei.weatherapp.core.worker.NotificationWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WorkerUptime(private val context: Context) : LifecycleEventObserver {
    lateinit var workManager: WorkManager

    private lateinit var outputWorkInfo: LiveData<List<WorkInfo>>

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_CREATE -> {
                workManager = WorkManager.getInstance(context)

                // Launch work manager job if there's any favourite
                // Also, if there's favourite city
                outputWorkInfo = workManager.getWorkInfosByTagLiveData(Constants.NOTIFICATION_MANAGER_TAG)

                val workAlreadyStarted = outputWorkInfo.value?.filter { workInfo ->
                    workInfo.tags.contains(Constants.NOTIFICATION_MANAGER_TAG)
                }

                if (workAlreadyStarted.isNullOrEmpty()) {
                    initialiseJobToNotifyOfFavourite(workManager)
                }

                Timber.d("OnCreate: workers - $outputWorkInfo")
            }

            Lifecycle.Event.ON_RESUME -> {
                Timber.d("OnResume: to pause notifications")
                // TODO pause notifications
            }

            else -> {
                // Nothing to handle for other cases.
                Timber.d("$event: Handle other cases.")
            }

        }
    }

    private fun initialiseJobToNotifyOfFavourite(workManager: WorkManager) {
        val notifyTemperatureWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
                .addTag(Constants.NOTIFICATION_MANAGER_TAG).build()

        workManager.enqueueUniquePeriodicWork(
            Constants.WORK_ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            notifyTemperatureWorkRequest
        )
    }
}