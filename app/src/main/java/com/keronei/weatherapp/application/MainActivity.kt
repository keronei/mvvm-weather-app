/*
 * Copyright 2021 Keronei Lincoln
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
package com.keronei.weatherapp.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.keronei.weatherapp.R
import com.keronei.weatherapp.core.worker.NotificationWorker
import com.keronei.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

/**
 *  Main Activity which is the Launcher Activity
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var activityMainViewBinding: ActivityMainBinding

    private lateinit var outputWorkInfos: LiveData<List<WorkInfo>>

    private val workManager = WorkManager.getInstance(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainViewBinding.root)

        // Launch work manager job if there's any favourite
        // Also, if there's favourite city
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(Constants.NOTIFICATION_MANAGER_TAG)

        val workAlreadyStarted = outputWorkInfos.value?.filter { workInfo ->
            workInfo.tags.contains(Constants.NOTIFICATION_MANAGER_TAG)
        }

        if (workAlreadyStarted.isNullOrEmpty()) {
            initialiseJobToNotifyOfFavourite()
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = this.getString(R.string.channel_name)
            val descriptionText = this.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initialiseJobToNotifyOfFavourite() {
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