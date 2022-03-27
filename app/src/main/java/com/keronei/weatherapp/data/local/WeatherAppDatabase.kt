package com.keronei.weatherapp.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.keronei.weatherapp.application.Constants.DATABASE_NAME
import com.keronei.weatherapp.data.model.CityObjEntity
import com.keronei.weatherapp.data.model.Forecast
import com.keronei.weatherapp.data.model.typeconverters.ObjectCollectionConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileDescriptor
import java.io.FileReader
import java.io.IOException

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
            context: Context,
            coroutineScope: CoroutineScope,
            csvMapper: CsvMapper
        ): WeatherAppDatabase {
            return databaseInstance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAppDatabase::class.java,
                    DATABASE_NAME
                ).addCallback(WeatherAppDatabaseCallBack(coroutineScope, csvMapper, context))
                    .build()
                databaseInstance = instance

                instance
            }
        }
    }

    private class WeatherAppDatabaseCallBack(
        private val scope: CoroutineScope,
        private val csvMapper: CsvMapper,
        private val context: Context

    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            databaseInstance?.let { readyInstance ->
                scope.launch(Dispatchers.IO) {
                    val instance = readyInstance.cityDao()
                    try {
                        val csvFile = getFileFromAssets(context, "worldcities.csv")
                        val readCities = readCsvFile<CityObjEntity>(csvFile)

                        readCities.forEach { readCity ->
                           val creationId = instance.createCity(readCity)
                            Timber.d("Created ${readCity.city_ascii} with identity $creationId")
                        }

                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }

                }

            }


        }

        @Throws(IOException::class)
        fun getFileFromAssets(context: Context, fileName: String): File =
            File(context.cacheDir, fileName)
                .also {
                    if (!it.exists()) {
                        it.outputStream().use { cache ->
                            context.assets.open(fileName).use { inputStream ->
                                inputStream.copyTo(cache)
                            }
                        }
                    }
                }

        inline fun <reified T> readCsvFile(fileName: File): List<T> {
            FileReader(fileName).use { reader ->
                return csvMapper
                    .readerFor(T::class.java)
                    .with(CsvSchema.emptySchema().withHeader())
                    .readValues<T>(reader)
                    .readAll()
                    .toList()
            }
        }
    }
}