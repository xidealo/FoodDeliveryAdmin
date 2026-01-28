package com.bunbeauty.domain.platform

import platform.Foundation.NSUUID

actual class UuidGenerator {
    actual fun generateUuid(): String = NSUUID().UUIDString()
}