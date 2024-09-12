package com.example.geminitest1.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "bike_preferences")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val dataStore = context.dataStore

    val bikesFlow: Flow<List<Bike>> = dataStore.data
        .map { preferences ->
            val bikeListString = preferences[BIKE_LIST]
            parseBikeList(bikeListString)
        }.flowOn(Dispatchers.IO)

    private fun parseBikeList(bikeListString: String?): List<Bike> {
        return bikeListString?.split(SEPARATOR)?.map {
            val parts = it.split(",")
            Bike(parts[0], parts[1], parts.getOrNull(2), parts.getOrNull(3))
        } ?: emptyList()
    }

    suspend fun saveBike(bike: Bike) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                val currentBikes = parseBikeList(preferences[BIKE_LIST]).toMutableList()
                val existingBike = currentBikes.find { it.make == bike.make && it.model == bike.model && it.year == bike.year }

                if (existingBike == null) {
                    currentBikes.add(bike)
                    preferences[BIKE_LIST] = currentBikes.joinToString(SEPARATOR) {
                        "${it.make},${it.model},${it.year ?: ""},${it.customName ?: ""}"
                    }
                }
            }
        }
    }

    suspend fun deleteBike(bike: Bike) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                val currentBikes = parseBikeList(preferences[BIKE_LIST]).toMutableList()
                currentBikes.removeIf { it.make == bike.make && it.model == bike.model && it.year == bike.year }
                preferences[BIKE_LIST] = currentBikes.joinToString(SEPARATOR) {
                    "${it.make},${it.model},${it.year ?: ""},${it.customName ?: ""}"
                }
            }
        }
    }

    companion object {
        private const val SEPARATOR = ";"
        private val BIKE_LIST = stringPreferencesKey("bike_list")
    }
}

data class Bike(
    val make: String,
    val model: String,
    val year: String?,
    val customName: String?
)

data class ErrorCode(
    val errorCode: String,
    val description: String,
    val possibleCauses: List<String>,
    val solutions: List<String>
)

