package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException

class ValidateMenuProductNameUseCase {

    operator fun invoke(name: String): String {
        return name.trim()
            .takeIf { value ->
                value.isNotEmpty()
            } ?: throw MenuProductNameException()
    }
}
