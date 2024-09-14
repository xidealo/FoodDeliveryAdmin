package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNutritionException
import javax.inject.Inject

class ValidateMenuProductNutritionUseCase @Inject constructor() {

    operator fun invoke(nutrition: String, units: String): Int? {
        if (nutrition.isBlank()) {
            return 0
        }

        val nutritionInt = nutrition.toIntOrNull()
        if (nutritionInt != null && units.isBlank()) {
            throw MenuProductNutritionException()
        }

        return nutritionInt
    }

}