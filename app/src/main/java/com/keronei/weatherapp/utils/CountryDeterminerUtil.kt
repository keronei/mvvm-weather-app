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

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import com.keronei.weatherapp.application.Constants.COUNTRY
import com.keronei.weatherapp.application.preference.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

internal object CountryDeterminerUtil {
    @SuppressLint("MissingPermission") // Has checks that's not reflected.
    fun getCountry(context: Context, dataStoreManager: DataStoreManager): String? {

        var country = runBlocking { dataStoreManager.getStringPreference(COUNTRY).first() }

        if (country != "") {
            return country
        }

        country = getCountryBasedOnSimCardOrNetwork(context) ?: ""

        if (country != "") {
            runBlocking {
                dataStoreManager.saveStringPreference(COUNTRY, country)
            }
            return country
        }

        return country
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private fun getCountryBasedOnSimCardOrNetwork(context: Context): String? {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                return simCountry.lowercase(Locale.US)
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.lowercase(Locale.US)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}