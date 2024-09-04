package com.bunbeauty.domain.feature.menu.common

import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import javax.inject.Inject

class GetSelectableCategoryListUseCase @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase
) {
    suspend operator fun invoke(selectedCategoryUuidList: List<String> = emptyList()): List<SelectableCategory> {
        return getCategoryListUseCase().map { category ->
            SelectableCategory(
                category = category,
                selected = selectedCategoryUuidList.contains(category.uuid)
            )
        }
    }
}
