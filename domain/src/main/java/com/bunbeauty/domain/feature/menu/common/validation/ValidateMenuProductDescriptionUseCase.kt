package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import javax.inject.Inject

class ValidateMenuProductDescriptionUseCase @Inject constructor() {

    operator fun invoke(description: String): String {
        return description.trim()
            .takeIf { value ->
                value.isNotEmpty()
            } ?: throw MenuProductDescriptionException()
    }
}
