package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException

class ValidateMenuProductNewPriceUseCase {
    operator fun invoke(newPrice: String): Int =
        newPrice
            .trim()
            .toIntOrNull()
            ?.takeIf { value ->
                value > 0
            } ?: throw MenuProductNewPriceException()
}
