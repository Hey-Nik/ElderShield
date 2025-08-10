
package com.safe.eldershield.core

data class ScanItem(
    val id: String,
    val from: String?,
    val preview: String,
    val type: String, // SMS or CALL
    var score: Int,
    var userLabel: String? = null // "spam" or "not_spam"
)

object Store {
    val results = mutableListOf<ScanItem>()
}
