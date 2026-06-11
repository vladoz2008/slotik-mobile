package com.slotik.mobile.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.BookingStatus
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject

data class PersistedSlotikSnapshot(
    val onboardingCompleted: Boolean = false,
    val isAuthorized: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val favorites: Set<String> = emptySet(),
)

class SlotikPreferencesStorage(context: Context) {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("slotik_preferences") },
    )

    suspend fun readSnapshot(): PersistedSlotikSnapshot {
        val preferences = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .first()

        return PersistedSlotikSnapshot(
            onboardingCompleted = preferences[OnboardingCompletedKey] ?: false,
            isAuthorized = preferences[IsAuthorizedKey] ?: false,
            bookings = decodeBookings(preferences[BookingsKey].orEmpty()),
            favorites = decodeFavorites(preferences[FavoritesKey].orEmpty()),
        )
    }

    suspend fun writeSnapshot(snapshot: PersistedSlotikSnapshot) {
        dataStore.edit { preferences ->
            preferences[OnboardingCompletedKey] = snapshot.onboardingCompleted
            preferences[IsAuthorizedKey] = snapshot.isAuthorized
            preferences[BookingsKey] = encodeBookings(snapshot.bookings)
            preferences[FavoritesKey] = snapshot.favorites.joinToString(separator = ",")
        }
    }

    private fun encodeBookings(bookings: List<Booking>): String {
        val jsonArray = JSONArray()
        bookings.forEach { booking ->
            jsonArray.put(
                JSONObject()
                    .put("id", booking.id)
                    .put("specialistId", booking.specialistId)
                    .put("specialistName", booking.specialistName)
                    .put("specialistSubtitle", booking.specialistSubtitle)
                    .put("serviceName", booking.serviceName)
                    .put("location", booking.location)
                    .put("date", booking.date.toString())
                    .put("startTime", booking.startTime.toString())
                    .put("endTime", booking.endTime.toString())
                    .put("price", booking.price)
                    .put("discount", booking.discount)
                    .put("totalPrice", booking.totalPrice)
                    .put("status", booking.status.name)
                    .put("comment", booking.comment)
            )
        }
        return jsonArray.toString()
    }

    private fun decodeBookings(raw: String): List<Booking> {
        if (raw.isBlank()) return emptyList()

        val jsonArray = JSONArray(raw)
        return buildList {
            repeat(jsonArray.length()) { index ->
                val item = jsonArray.getJSONObject(index)
                add(
                    Booking(
                        id = item.getString("id"),
                        specialistId = item.getString("specialistId"),
                        specialistName = item.getString("specialistName"),
                        specialistSubtitle = item.getString("specialistSubtitle"),
                        serviceName = item.getString("serviceName"),
                        location = item.getString("location"),
                        date = LocalDate.parse(item.getString("date")),
                        startTime = LocalTime.parse(item.getString("startTime")),
                        endTime = LocalTime.parse(item.getString("endTime")),
                        price = item.getInt("price"),
                        discount = item.getInt("discount"),
                        totalPrice = item.getInt("totalPrice"),
                        status = BookingStatus.valueOf(item.getString("status")),
                        comment = item.optString("comment"),
                    ),
                )
            }
        }
    }

    private fun decodeFavorites(raw: String): Set<String> {
        if (raw.isBlank()) return emptySet()
        return raw.split(',').map(String::trim).filter(String::isNotBlank).toSet()
    }

    private companion object {
        val OnboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
        val IsAuthorizedKey = booleanPreferencesKey("is_authorized")
        val BookingsKey = stringPreferencesKey("bookings_json")
        val FavoritesKey = stringPreferencesKey("favorite_specialists")
    }
}
