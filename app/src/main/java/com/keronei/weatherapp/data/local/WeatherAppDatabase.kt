package com.keronei.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.keronei.weatherapp.application.Constants.DATABASE_NAME
import com.keronei.weatherapp.data.model.CityObjEntity

@Database(
    entities = [CityObjEntity::class], version = 1, exportSchema = true,
    // autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class WeatherAppDatabase : RoomDatabase() {
    // abstract fun forecastDao(): ForecastDao

    abstract fun cityDao(): CityDao

    companion object {

        @Volatile
        private var databaseInstance: WeatherAppDatabase? = null

        fun buildDatabase(context: Context): WeatherAppDatabase {
            return databaseInstance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAppDatabase::class.java,
                    DATABASE_NAME
                ).createFromAsset(DATABASE_NAME).build()
                databaseInstance = instance

                instance
            }
        }
    }
}