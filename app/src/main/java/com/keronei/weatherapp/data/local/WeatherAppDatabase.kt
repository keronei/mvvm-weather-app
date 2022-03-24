package com.keronei.weatherapp.data.local

import android.content.Context
import androidx.room.*
import com.keronei.weatherapp.application.Constants.DATABASE_NAME
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.data.model.typeconverters.ObjectCollectionConverter

@Database(
    entities = [CityObjEntity::class, Forecast::class], version = 2, exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
@TypeConverters(ObjectCollectionConverter::class)
abstract class WeatherAppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao

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