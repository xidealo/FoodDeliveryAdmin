package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import javax.inject.Inject

class ValidateMenuProductNameUseCase @Inject constructor() {

    operator fun invoke(name: String): String {
        return name.takeIf { value ->
            value.isNotBlank()
        } ?: throw MenuProductNameException()
    }
}
