package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory

class GetSelectableCategoryListUseCase(
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
