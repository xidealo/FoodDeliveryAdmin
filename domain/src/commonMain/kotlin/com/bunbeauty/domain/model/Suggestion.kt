package com.bunbeauty.domain.model

data class Suggestion(
    val id: String,
    val value: String,
) {
    companion object {
        val mock =
            Suggestion(
                id = "",
                value = "",
            )
    }
}
