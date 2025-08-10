
package com.safe.eldershield.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.safe.eldershield.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val AppDataStore by preferencesDataStore(name = "elder_prefs")

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val phrase = findViewById<EditText>(R.id.secretPhrase)
        val phone = findViewById<EditText>(R.id.caregiverPhone)
        val save = findViewById<Button>(R.id.saveBtn)

        val PHRASE = stringPreferencesKey("secret_phrase")
        val CARE = stringPreferencesKey("caregiver_phone")
        val scope = CoroutineScope(Dispatchers.IO)

        save.setOnClickListener {
            scope.launch {
                applicationContext.AppDataStore.edit { p ->
                    p[PHRASE] = phrase.text.toString().trim()
                    p[CARE] = phone.text.toString().trim()
                }
            }
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
