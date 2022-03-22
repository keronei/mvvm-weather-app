package com.keronei.weatherapp.utils

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

fun appHasLocationPermission(context: Context): Boolean {
    return EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION)
}