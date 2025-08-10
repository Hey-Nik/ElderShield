package com.safe.eldershield.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object AppDataStore {
    // Guessing common toggle keys. If your UI expects different names, tell me and I'll match them.
    val KEY_SCAN_SMS = booleanPreferencesKey("scan_sms")
    val KEY_SCAN_CALLS = booleanPreferencesKey("scan_calls")
    val KEY_SHOW_NOTIFICATIONS = booleanPreferencesKey("show_notifications")

    fun scanSmsFlow(ds: DataStore<Preferences>): Flow<Boolean> =
        ds.data.map { it[KEY_SCAN_SMS] ?: true }

    fun scanCallsFlow(ds: DataStore<Preferences>): Flow<Boolean> =
        ds.data.map { it[KEY_SCAN_CALLS] ?: true }

    fun showNotificationsFlow(ds: DataStore<Preferences>): Flow<Boolean> =
        ds.data.map { it[KEY_SHOW_NOTIFICATIONS] ?: true }

    suspend fun setScanSms(ds: DataStore<Preferences>, value: Boolean) {
        ds.edit { it[KEY_SCAN_SMS] = value }
    }

    suspend fun setScanCalls(ds: DataStore<Preferences>, value: Boolean) {
        ds.edit { it[KEY_SCAN_CALLS] = value }
    }

    suspend fun setShowNotifications(ds: DataStore<Preferences>, value: Boolean) {
        ds.edit { it[KEY_SHOW_NOTIFICATIONS] = value }
    }
}