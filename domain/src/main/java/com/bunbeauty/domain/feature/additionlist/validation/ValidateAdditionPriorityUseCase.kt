package com.bunbeauty.domain.feature.additionlist.validation

import com.bunbeauty.domain.feature.additionlist.exception.AdditionPriorityException
import javax.inject.Inject

class ValidateAdditionPriorityUseCase @Inject constructor() {

    operator fun invoke(priority: String): String {
        return priority.trim()
            .takeIf { value ->
                value.isNotEmpty()
            } ?: throw AdditionPriorityException()
    }
}
