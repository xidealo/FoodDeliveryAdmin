package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionLongException

class ValidateMenuProductDescriptionUseCase {
    operator fun invoke(description: String): String {
        val cleanedDescription = description.trim()

        return when {
            cleanedDescription.isEmpty() -> throw MenuProductDescriptionException()
            cleanedDescription.length > 512 -> throw MenuProductDescriptionLongException()
            else -> cleanedDescription
        }
    }
}
