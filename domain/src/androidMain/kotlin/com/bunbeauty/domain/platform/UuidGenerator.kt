package com.bunbeauty.domain.platform

import java.util.UUID

actual class UuidGenerator {
    actual fun generateUuid(): String = UUID.randomUUID().toString()
}
