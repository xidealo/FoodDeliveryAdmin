package com.bunbeauty.domain.feature.additionlist.common.validation

import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import javax.inject.Inject

class ValidateAdditionPriorityUseCase @Inject constructor() {

    operator fun invoke(priority: String): String {
        return priority.trim()
            .takeIf { value ->
                value.isNotEmpty()
            } ?: throw AdditionPriorityException()
    }
}