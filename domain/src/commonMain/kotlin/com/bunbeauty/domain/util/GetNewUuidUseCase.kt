package com.bunbeauty.domain.util

import com.bunbeauty.domain.platform.UuidGenerator

class GetNewUuidUseCase(
    private val platformUuid: UuidGenerator
) {
    operator fun invoke(): String = platformUuid.generateUuid()

}
