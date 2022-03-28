/*
 * Copyright 2022 GradleBuildPlugins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.weatherapp.utils

import android.Manifest
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.keronei.weatherapp.application.Constants.KELVIN_CONVERSION
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

fun appHasLocationPermission(context: Context): Boolean =
    EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION)

fun Double.toCelsius(): Double = (this - KELVIN_CONVERSION)

inline fun SearchView.onQueryTextChanged(crossinline onQueryTextChanged: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            onQueryTextChanged(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}

fun getDrawableWithName(context: Context, drawableName: String): Drawable? {
    return AppCompatResources.getDrawable(
        context,
        context.resources.getIdentifier(
            drawableName,
            "drawable",
            context.packageName
        )
    )
}

fun String.capitaliseFirstCharacter(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(
                Locale.getDefault()
            )
        } else {
            it.toString()
        }
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun <T : View> T.hideIf(condition: (T) -> Boolean) {
    if (condition(this)) {
        hide()
    } else {
        show()
    }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, duration)
}

fun Int.fromUnixTimestamp(): Long = (this * 1000).toLong()