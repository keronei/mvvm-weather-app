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
package com.keronei.weatherapp.application.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveBooleanPreference(key: String, value: Boolean) {
        dataStore.edit { preference ->
            preference[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun saveStringPreference(key: String, value: String) {
        dataStore.edit { preference ->
            preference[stringPreferencesKey(key)] = value
        }
    }

    fun getBooleanPreference(key: String): Flow<Boolean> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                }
            }.map { preferences ->

                preferences[booleanPreferencesKey(key)] ?: false
            }

    fun getStringPreference(key: String): Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preference ->
        preference[stringPreferencesKey(key)] ?: ""
    }

    suspend fun cleanUpPreferenceAtLogOut() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveIntPreference(key: String, id: Int) {
        dataStore.edit { preference ->
            preference[intPreferencesKey(key)] = id
        }
    }

    fun getIntPreference(key: String): Flow<Int> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preference ->
        preference[intPreferencesKey(key)] ?: -1
    }
}