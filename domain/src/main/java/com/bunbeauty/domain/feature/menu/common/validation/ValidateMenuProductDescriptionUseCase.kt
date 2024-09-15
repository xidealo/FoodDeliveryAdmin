package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import javax.inject.Inject

class ValidateMenuProductDescriptionUseCase @Inject constructor() {

    operator fun invoke(description: String): String {
        return description.takeIf { value ->
            value.isNotBlank()
        } ?: throw MenuProductDescriptionException()
    }
}
