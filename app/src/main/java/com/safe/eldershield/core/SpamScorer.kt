package com.safe.eldershield.core

class SpamScorer {

    // Use a RAW string (triple quotes) so backslashes don't need escaping.
    // Add/remove words as you like.
    private val spamRegex = Regex(
        """\b(free|win|prize|lottery|claim|urgent|click|link|offer|credit|loan|otp|verify)\b""",
        RegexOption.IGNORE_CASE
    )

    fun isSpam(text: String): Boolean {
        return spamRegex.containsMatchIn(text)
    }

    fun score(text: String): Int {
        // simple score: +1 per match
        return spamRegex.findAll(text).count()
    }
}