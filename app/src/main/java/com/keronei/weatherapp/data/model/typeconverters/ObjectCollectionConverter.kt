package com.keronei.weatherapp.data.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.keronei.weatherapp.data.model.Alert
import com.keronei.weatherapp.data.model.Daily
import com.keronei.weatherapp.data.model.Hourly


class ObjectCollectionConverter {
    private val gsonInstance = Gson()

    @TypeConverter
    fun fromHourlyList(hourly: List<Hourly>): String {
        return gsonInstance.toJson(hourly)
    }

    @TypeConverter
    fun toHourList(json: String?): List<Hourly> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<Hourly>::class.java).asList()
        }
    }

    @TypeConverter
    fun fromDailyList(daily: List<Daily>): String {
        return gsonInstance.toJson(daily)
    }

    @TypeConverter
    fun toDailyList(json: String?): List<Daily> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<Daily>::class.java).asList()
        }
    }

    @TypeConverter
    fun fromAlertsList(alerts: List<Alert>?): String {
        return if(alerts == null) "" else gsonInstance.toJson(alerts)
    }

    @TypeConverter
    fun toAlertList(json: String?): List<Alert> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<Alert>::class.java).asList()
        }
    }
}