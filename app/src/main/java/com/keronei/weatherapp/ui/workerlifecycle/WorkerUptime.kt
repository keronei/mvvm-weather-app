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
import com.keronei.weatherapp.application.Constants.NOTIFICATION_MANAGER_TAG
import com.keronei.weatherapp.core.worker.NotificationWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WorkerUptime(private val context: Context) : LifecycleEventObserver {
    lateinit var workManager: WorkManager

    private lateinit var outputWorkInfo: LiveData<List<WorkInfo>>

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                workManager = WorkManager.getInstance(context)

                // Launch work manager job if there's any favourite
                // Also, if there's favourite city
                outputWorkInfo = workManager.getWorkInfosByTagLiveData(NOTIFICATION_MANAGER_TAG)

                Timber.d("OnCreate: workers - $outputWorkInfo")
            }

            Lifecycle.Event.ON_RESUME -> {
                pauseWorkerIfRunning()
            }

            Lifecycle.Event.ON_PAUSE -> {
                startWorkerIfNotRunning()
            }

            else -> {
                // Nothing to handle for other cases.
                Timber.d("$event: Handle other cases.")
            }
        }
    }

    private fun pauseWorkerIfRunning() {
        val workAlreadyStarted = outputWorkInfo.value?.filter { workInfo ->
            workInfo.tags.contains(NOTIFICATION_MANAGER_TAG)
        }

        if (workAlreadyStarted?.isNotEmpty() == true) {
            workManager.cancelAllWorkByTag(NOTIFICATION_MANAGER_TAG)
        }
    }

    private fun startWorkerIfNotRunning() {
        val workAlreadyStarted = outputWorkInfo.value?.filter { workInfo ->
            workInfo.tags.contains(NOTIFICATION_MANAGER_TAG)
        }

        if (workAlreadyStarted.isNullOrEmpty()) {
            initialiseJobToNotifyOfFavourite(workManager)
        }
    }

    private fun initialiseJobToNotifyOfFavourite(workManager: WorkManager) {
        val notifyTemperatureWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.HOURS) // start in the next hour.
                .addTag(NOTIFICATION_MANAGER_TAG).build()

        workManager.enqueueUniquePeriodicWork(
            Constants.WORK_ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            notifyTemperatureWorkRequest
        )
    }
}