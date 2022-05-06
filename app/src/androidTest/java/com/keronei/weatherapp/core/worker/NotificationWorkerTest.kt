package com.keronei.weatherapp.core.worker

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.keronei.weatherapp.application.WeatherApp
import com.keronei.weatherapp.domain.CitiesRepository
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

//@CustomTestApplication(WeatherApp::class)
//interface HiltTestApplication

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class NotificationWorkerTest {
    lateinit var appContext: Context
    lateinit var citiesRepository: CitiesRepository
    lateinit var scope: CoroutineScope

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext() //InstrumentationRegistry.getInstrumentation().targetContext

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        //citiesRepository = FakeInstrumentationCitiesRepositoryImpl()

        //scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        WorkManagerTestInitHelper.initializeTestWorkManager(appContext, config)

    }

    @Test
    @Throws(Exception::class)
    fun testNotificationIsEnqueuedWhenAFavoriteExists(){
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
            .build()

        val workManager = WorkManager.getInstance(ApplicationProvider.getApplicationContext())
        val testDriver = WorkManagerTestInitHelper.getTestDriver(ApplicationProvider.getApplicationContext())

        workManager.enqueue(workRequest).result.get()
        testDriver?.setPeriodDelayMet(workRequest.id)

        val workInfo = workManager.getWorkInfoById(workRequest.id).get()

        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }
}