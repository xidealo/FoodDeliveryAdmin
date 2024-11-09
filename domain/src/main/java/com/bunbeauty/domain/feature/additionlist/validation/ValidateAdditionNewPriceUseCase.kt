package com.bunbeauty.domain.feature.additionlist.validation

import com.bunbeauty.domain.exception.updateaddition.AdditionPriceException
import javax.inject.Inject

class ValidateAdditionNewPriceUseCase @Inject constructor() {

    operator fun invoke(newPrice: String): Int {
        return newPrice.trim()
            .toIntOrNull()
            ?.takeIf { value ->
                value > 0
            } ?: throw AdditionPriceException()
    }
}
