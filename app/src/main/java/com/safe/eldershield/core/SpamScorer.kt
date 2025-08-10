package com.safe.eldershield.core

object SpamScorer {

    // Simple patterns; triple-quoted strings avoid bad escape sequences.
    private val patterns = listOf(
        Regex("""\bprize\b""", RegexOption.IGNORE_CASE),
        Regex("""\bwin(?:ner|ning)?\b""", RegexOption.IGNORE_CASE),
        Regex("""\bOTP\b""", RegexOption.IGNORE_CASE),
        Regex("""\baccount\b""", RegexOption.IGNORE_CASE),
        Regex("""http[s]?://\S+""", RegexOption.IGNORE_CASE)
    )

    /**
     * Returns a score from 0..100 based on presence of suspicious patterns.
     */
    fun score(text: String?): Int {
        val t = text?.trim().orEmpty()
        if (t.isEmpty()) return 0
        var s = 0
        for (rx in patterns) if (rx.containsMatchIn(t)) s += 20
        return s.coerceIn(0, 100)
    }
}