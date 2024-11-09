package com.bunbeauty.domain.feature.additionlist.validation

import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import javax.inject.Inject

class ValidateAdditionNameUseCase @Inject constructor() {

    operator fun invoke(name: String): String {
        return name.trim()
            .takeIf { value ->
                value.isNotEmpty()
            } ?: throw AdditionNameException()
    }
}
