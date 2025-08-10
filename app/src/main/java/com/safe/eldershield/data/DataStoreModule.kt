package com.safe.eldershield.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// MUST be top-level and an extension on Context (not inside an Activity or object)
val Context.dataStore by preferencesDataStore(name = "settings")