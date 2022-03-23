package com.keronei.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import com.keronei.weatherapp.application.Constants.CITIES_JSON_FILE_NAME
import com.keronei.weatherapp.data.model.CityObjEntity
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.InputStream
import java.nio.charset.Charset

@Database(entities = [CityObjEntity::class], version = 1, exportSchema = true)
abstract class WeatherAppDatabase : RoomDatabase() {
    //abstract fun forecastDao(): ForecastDao

    abstract fun cityDao(): CityDao

    companion object {

        @Volatile
        private var databaseInstance: WeatherAppDatabase? = null

        fun buildDatabase(context: Context, scope: CoroutineScope): WeatherAppDatabase {
            return databaseInstance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAppDatabase::class.java,
                    "weatherappdatabase.db"
                ).addCallback(WeatherAppDatabaseCallBack(scope, context)).build()
                databaseInstance = instance

                instance
            }
        }
    }


    private class WeatherAppDatabaseCallBack(
        private val scope: CoroutineScope,
        val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            databaseInstance?.let { readyInstance ->
                scope.launch(Dispatchers.IO) {
                    val instance = readyInstance.cityDao()

                    val inputStream: InputStream =
                        context.resources.assets.open(CITIES_JSON_FILE_NAME)

                    val size: Int = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.read(buffer)
                    inputStream.close()
                    val json = String(buffer, Charset.forName("UTF-8"))

                    val gson = GsonBuilder().create()

                    try {
                        val readCities = gson.fromJson(json, Array<CityObjEntity>::class.java)

                        if (readCities != null) {
                            readCities.forEach { city ->
                                async { instance.createCity(city) }
                            }
                            Timber.d("Read cities count -> ${readCities.size}")
                        } else {
                            Timber.d("Read cities is null")
                        }

                        Timber.d("Read cities count -> ${readCities?.size}")
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }

            }

        }
    }
}