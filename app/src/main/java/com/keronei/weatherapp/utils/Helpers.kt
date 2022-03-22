package com.keronei.weatherapp.utils

import android.Manifest
import android.content.Context
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

fun appHasLocationPermission(context: Context): Boolean {
    return EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION)
}

suspend fun readCitiesFromJson(context: Context): JSONObject? {
    val json: String? = try {
        val inputStream: InputStream = context.resources.assets.open("city.list.min.json")
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return if (json == null) null else JSONObject(json)
}