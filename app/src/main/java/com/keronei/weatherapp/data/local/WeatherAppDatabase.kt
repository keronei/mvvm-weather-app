package com.keronei.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database()
abstract class WeatherAppDatabase {
    abstract fun forecastDao(): ForecastDao

    companion object {

        @Volatile
        private var databaseInstance: WeatherAppDatabase? = null

        fun buildDatabase(context: Context, scope: CoroutineScope): WeatherAppDatabase {
            return databaseInstance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAppDatabase::class.java,
                    "weatherappdatabase.db"
                ).addCallback(WeatherAppDatabaseCallBack(scope)).build()
                databaseInstance = instance

                instance
            }
        }
    }


    private class WeatherAppDatabaseCallBack(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            databaseInstance?.let { readyInstance ->
                scope.launch {
                    val instance = readyInstance.forecastDao()
                    instance
                }

            }

        }
    }
}