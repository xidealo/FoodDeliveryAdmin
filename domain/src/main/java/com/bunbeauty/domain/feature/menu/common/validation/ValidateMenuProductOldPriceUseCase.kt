package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import javax.inject.Inject

class ValidateMenuProductOldPriceUseCase @Inject constructor() {

    operator fun invoke(oldPrice: String, newPrice: Int): Int? {
        if (oldPrice.isBlank()) {
            return 0
        }

        val oldPriceInt = oldPrice.toIntOrNull()
        if (oldPriceInt != null && oldPriceInt <= newPrice) {
            throw MenuProductOldPriceException()
        }

        return oldPriceInt
    }

}