package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory

class ValidateMenuProductCategoriesUseCase {
    operator fun invoke(categories: List<SelectableCategory>): List<SelectableCategory> =
        categories.takeIf {
            categories.isNotEmpty()
        } ?: throw MenuProductCategoriesException()
}
