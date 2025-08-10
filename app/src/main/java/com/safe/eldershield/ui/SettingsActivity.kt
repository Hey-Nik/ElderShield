package com.safe.eldershield.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.safe.eldershield.R
import com.safe.eldershield.data.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    private val KEY_PHRASE = stringPreferencesKey("secret_phrase")
    private val KEY_CAREGIVER = stringPreferencesKey("caregiver_phone")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val phraseEt = findViewById<EditText>(R.id.secretPhrase)
        val phoneEt = findViewById<EditText>(R.id.caregiverPhone)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        saveBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                applicationContext.dataStore.edit { prefs ->
                    prefs[KEY_PHRASE] = phraseEt.text.toString().trim()
                    prefs[KEY_CAREGIVER] = phoneEt.text.toString().trim()
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SettingsActivity,
                        getString(R.string.saved),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}