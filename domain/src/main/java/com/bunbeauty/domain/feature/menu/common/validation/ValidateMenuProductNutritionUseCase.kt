package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNutritionException
import javax.inject.Inject

class ValidateMenuProductNutritionUseCase @Inject constructor() {

    operator fun invoke(nutrition: String, units: String): Int? {
        val nutritionInt = nutrition.trim().toIntOrNull()
        if (nutritionInt != null && units.isEmpty()) {
            throw MenuProductNutritionException()
        }

        return nutritionInt
    }
}
