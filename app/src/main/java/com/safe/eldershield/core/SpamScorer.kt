
package com.safe.eldershield.core

import kotlin.math.min

object SpamScorer {
    // Basic heuristics; later we can plug in an on-device ML model.
    private val suspiciousWords = listOf(
        "otp","urgent","verify","account","bank","lottery","prize","refund",
        "blocked","deactivated","update","kYC","kyc","click","link","bit.ly","tinyurl",
        "upi","gift","win","password","PAN","Aadhaar","loan","crypto","investment"
    )

    fun score(text: String, from: String?): Int {
        var score = 0
        val t = text.lowercase()

        // URLs
        val urlRegex = "(https?://|www\.|bit\.ly|tinyurl|wa\.me)".toRegex()
        if (urlRegex.containsMatchIn(t)) score += 30

        // Keywords
        val hits = suspiciousWords.count { t.contains(it.lowercase()) }
        score += min(50, hits * 7)

        // Non-local numbers, short codes
        if (from != null) {
            val digits = from.filter { it.isDigit() }
            if (digits.length < 8) score += 10
            if (!from.startsWith("+") && digits.length >= 10) score += 5
        }

        // Excess punctuation or emojis
        if (t.count { it == '!' } >= 3) score += 10

        return score.coerceIn(0, 100)
    }
}
