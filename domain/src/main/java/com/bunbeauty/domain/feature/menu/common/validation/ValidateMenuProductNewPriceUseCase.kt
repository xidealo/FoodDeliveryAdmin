package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import javax.inject.Inject

class ValidateMenuProductNewPriceUseCase @Inject constructor() {

    operator fun invoke(newPrice: String): Int {
        return newPrice.trim()
            .toIntOrNull()
            ?.takeIf { value ->
                value > 0
            } ?: throw MenuProductNewPriceException()
    }
}
