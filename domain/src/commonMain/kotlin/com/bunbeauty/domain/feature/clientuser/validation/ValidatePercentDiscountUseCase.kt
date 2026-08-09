package com.bunbeauty.domain.feature.clientuser.validation

import com.bunbeauty.domain.feature.clientuser.exception.PercentDiscountException

class ValidatePercentDiscountUseCase {
    operator fun invoke(percentDiscount: Int): Int {
        if (percentDiscount in MIN_PERCENT_DISCOUNT..MAX_PERCENT_DISCOUNT) {
            return percentDiscount
        } else {
            throw PercentDiscountException()
        }
    }

    private companion object {
        const val MIN_PERCENT_DISCOUNT = 0
        const val MAX_PERCENT_DISCOUNT = 99
    }
}
