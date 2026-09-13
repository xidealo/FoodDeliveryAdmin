package com.bunbeauty.shared.feature.order

import common.Constants.GOOGLE_MAPS_DIR_LINK

internal fun buildGoogleMapsDirUrl(mapQuery: String): String = GOOGLE_MAPS_DIR_LINK + encodeUrlQuery(mapQuery)

internal fun encodeUrlQuery(value: String): String =
    buildString {
        value.encodeToByteArray().forEach { byte ->
            val unsigned = byte.toInt() and 0xFF
            val char = unsigned.toChar()
            if (isUrlUnreserved(char)) {
                append(char)
            } else {
                append('%')
                append(unsigned.toString(16).uppercase().padStart(2, '0'))
            }
        }
    }

private fun isUrlUnreserved(char: Char): Boolean =
    char in 'A'..'Z' ||
        char in 'a'..'z' ||
        char in '0'..'9' ||
        char == '-' ||
        char == '_' ||
        char == '.' ||
        char == '~'
