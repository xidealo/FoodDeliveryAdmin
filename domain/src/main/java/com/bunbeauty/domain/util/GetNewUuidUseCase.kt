package com.bunbeauty.domain.util

import java.util.UUID


class GetNewUuidUseCase {
    operator fun invoke(): String {
        return UUID.randomUUID().toString()
    }
}
