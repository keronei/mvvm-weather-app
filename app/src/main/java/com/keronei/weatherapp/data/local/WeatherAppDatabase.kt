package com.keronei.weatherapp.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.keronei.weatherapp.application.Constants.DATABASE_NAME
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.data.model.typeconverters.ObjectCollectionConverter

@Database(
    entities = [CityObjEntity::class, Forecast::class], version = 3, exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(
        from = 2,
        to = 3,
        spec = WeatherAppDatabase.AutoMigrationSpecFrom2to3::class
    )]
)
@TypeConverters(ObjectCollectionConverter::class)
abstract class WeatherAppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao

    abstract fun cityDao(): CityDao

    @RenameColumn(fromColumnName = "id", toColumnName = "identity", tableName = "CityObjEntity")
    @DeleteColumn(tableName = "CityObjEntity", columnName = "coord")
    @RenameColumn(fromColumnName = "name", toColumnName = "city_ascii", tableName = "CityObjEntity")
    @RenameColumn(fromColumnName = "state", toColumnName = "city", tableName = "CityObjEntity")
    @RenameColumn(fromColumnName = "lon", toColumnName = "lng", tableName = "CityObjEntity")
    class AutoMigrationSpecFrom2to3 : AutoMigrationSpec

    companion object {

        @Volatile
        private var databaseInstance: WeatherAppDatabase? = null

        fun buildDatabase(
            context: Context
        ): WeatherAppDatabase {
            return databaseInstance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAppDatabase::class.java,
                    DATABASE_NAME
                ).createFromAsset(DATABASE_NAME)
                    .build()
                databaseInstance = instance

                instance
            }
        }
    }
}