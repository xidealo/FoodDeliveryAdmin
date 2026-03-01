package com.bunbeauty.presentation.feature.menulist.common

import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory

data class CategoriesFieldData(
    override val value: List<SelectableCategory>,
    override val isError: Boolean,
) : FieldData<List<SelectableCategory>>() {
    val selectedCategoryList: List<SelectableCategory>
        get() {
            return value.filter { category ->
                category.selected
            }
        }
}
