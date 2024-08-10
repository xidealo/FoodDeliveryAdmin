package com.bunbeauty.domain.feature.menu.addmenuproduct

import com.bunbeauty.domain.model.category.SelectableCategory
import javax.inject.Inject

class GetSelectableCategoryListUseCase @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
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
