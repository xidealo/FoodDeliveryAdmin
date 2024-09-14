package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import javax.inject.Inject

class ValidateMenuProductCategoriesUseCase @Inject constructor() {

    operator fun invoke(categories: List<SelectableCategory>): List<SelectableCategory> {
        return categories.takeIf {
            categories.isNotEmpty()
        } ?: throw MenuProductCategoriesException()
    }

}