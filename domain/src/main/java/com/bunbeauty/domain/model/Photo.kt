package com.bunbeauty.domain.model

data class Photo(
    val url: String
) {
    companion object {
        val mock = Photo(
            url = ""
        )
    }
}
