
package com.safe.eldershield.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.safe.eldershield.R

class MainActivity : AppCompatActivity() {

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* refresh after permission */ refreshStatuses() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindSteps()
        findViewById<Button>(R.id.scanBtn).setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        findViewById<Button>(R.id.reviewBtn).setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStatuses()
    }

    private fun bindSteps() {
        val stepsContainer = findViewById<LinearLayout>(R.id.steps)
        val steps = listOf(
            Step(
                getString(R.string.step1),
                { isNotificationAccessEnabled() },
                { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
            ),
            Step(
                getString(R.string.step2),
                { Settings.canDrawOverlays(this) },
                {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                    startActivity(intent)
                }
            ),
            Step(
                getString(R.string.step3),
                { isAccessibilityEnabled() },
                { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
            ),
            Step(
                getString(R.string.step4),
                { true },
                { startActivity(Intent(this, SettingsActivity::class.java)) }
            ),
            Step(
                getString(R.string.step5),
                { hasScanPerms() },
                { requestPermissions.launch(arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_CONTACTS
                )) }
            )
        )

        // For each included row
        for (i in 0 until stepsContainer.childCount) {
            val row = stepsContainer.getChildAt(i)
            val step = steps[i]
            row.findViewById<TextView>(R.id.stepText).text = step.text
            row.findViewById<TextView>(R.id.statusText).text =
                if (step.statusProvider()) getString(R.string.configured) else getString(R.string.not_configured)
            row.findViewById<Button>(R.id.actionBtn).setOnClickListener { step.action() }
        }
    }

    private fun refreshStatuses() = bindSteps()

    private fun hasScanPerms(): Boolean {
        val sms = checkSelfPermission(Manifest.permission.READ_SMS) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val calls = checkSelfPermission(Manifest.permission.READ_CALL_LOG) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val contacts = checkSelfPermission(Manifest.permission.READ_CONTACTS) == android.content.pm.PackageManager.PERMISSION_GRANTED
        return sms && calls && contacts
    }

    private fun isNotificationAccessEnabled(): Boolean {
        val sets = NotificationManagerCompat.getEnabledListenerPackages(this)
        return sets.contains(packageName)
    }

    private fun isAccessibilityEnabled(): Boolean {
        val enabled = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
        return enabled.contains(packageName)
    }

    data class Step(val text: String, val statusProvider: () -> Boolean, val action: () -> Unit)
}
