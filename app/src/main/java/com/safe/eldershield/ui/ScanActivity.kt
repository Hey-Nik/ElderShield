
package com.safe.eldershield.ui

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.Telephony
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.safe.eldershield.R
import com.safe.eldershield.core.ScanItem
import com.safe.eldershield.core.SpamScorer
import com.safe.eldershield.core.Store

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val text = findViewById<TextView>(R.id.progressText)

        Store.results.clear()
        var count = 0

        // Scan SMS inbox
        try {
            val cursor: Cursor? = contentResolver.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                arrayOf(Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.BODY),
                null, null, Telephony.Sms.DEFAULT_SORT_ORDER
            )
            cursor?.use {
                val total = it.count.coerceAtLeast(1)
                while (it.moveToNext()) {
                    val id = it.getString(0)
                    val from = it.getString(1)
                    val body = it.getString(2) ?: ""
                    val s = SpamScorer.score(body, from)
                    Store.results.add(ScanItem("sms_$id", from, body.take(120), "SMS", s))
                    count++
                    progress.progress = (count * 100 / total).coerceIn(0, 100)
                    text.text = getString(R.string.sms_count, count)
                }
            }
        } catch (_: Exception) {}

        // Scan Call log (most recent 200)
        try {
            val cursor = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.TYPE),
                null, null, CallLog.Calls.DEFAULT_SORT_ORDER + " LIMIT 200"
            )
            cursor?.use {
                var local = 0
                while (it.moveToNext()) {
                    val id = it.getString(0)
                    val num = it.getString(1) ?: ""
                    val type = it.getInt(2)
                    val label = when (type) {
                        CallLog.Calls.MISSED_TYPE -> "MISSED"
                        CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                        CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                        else -> "CALL"
                    }
                    val s = SpamScorer.score(label, num)
                    Store.results.add(ScanItem("call_$id", num, "$label: $num", "CALL", s))
                    local++
                }
                text.text = getString(R.string.call_count, local)
            }
        } catch (_: Exception) {}

        text.text = getString(R.string.done)
        finish() // return to previous screen
    }
}
