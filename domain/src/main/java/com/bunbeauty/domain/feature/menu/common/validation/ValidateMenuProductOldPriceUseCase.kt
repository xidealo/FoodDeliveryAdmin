package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import javax.inject.Inject

class ValidateMenuProductOldPriceUseCase @Inject constructor() {

    operator fun invoke(oldPrice: String, newPrice: Int): Int? {
        val oldPriceInt = oldPrice.trim().toIntOrNull()
        if (oldPriceInt != null && oldPriceInt <= newPrice) {
            throw MenuProductOldPriceException()
        }

        return oldPriceInt
    }
}
