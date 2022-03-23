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
