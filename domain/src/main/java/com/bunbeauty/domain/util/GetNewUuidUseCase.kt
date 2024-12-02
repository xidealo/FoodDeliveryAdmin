package com.bunbeauty.domain.util

import java.util.UUID
import javax.inject.Inject

class GetNewUuidUseCase @Inject constructor() {
    operator fun invoke(): String {
        return UUID.randomUUID().toString()
    }
}
