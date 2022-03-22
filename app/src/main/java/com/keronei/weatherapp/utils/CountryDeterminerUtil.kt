package com.keronei.weatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.telephony.TelephonyManager
import com.keronei.weatherapp.application.Constants.COUNTRY
import com.keronei.weatherapp.application.preference.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.*

internal object CountryHelper {
    @SuppressLint("MissingPermission") // Has checks that's not reflected.
    fun getCountry(context: Context, dataStoreManager: DataStoreManager): String? {

        var country = runBlocking { dataStoreManager.getStringPreference(COUNTRY).first() }

        if (country != "") {
            return country
        }
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (appHasLocationPermission(context)) {
            var location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (location == null) {
                Timber.w("Couldn't get location from network and gps providers")
                return null
            }
            val gcd = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>?
            try {
                addresses = gcd.getFromLocation(
                    location.latitude,
                    location.longitude, 1
                )
                if (addresses != null && addresses.isNotEmpty()) {
                    country = addresses[0].countryName
                    if (country != null) {
                        runBlocking {
                            dataStoreManager.saveStringPreference(COUNTRY, country)
                        }
                        return country
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {

            country = getCountryBasedOnSimCardOrNetwork(context) ?: ""

            if (country != "") {
                runBlocking {
                    dataStoreManager.saveStringPreference(COUNTRY, country)
                }
                return country
            }
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
        }
        return null
    }
}