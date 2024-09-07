package com.bunbeauty.fooddeliveryadmin.screen.menulist.common

import androidx.annotation.StringRes
import com.bunbeauty.common.Constants
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.presentation.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData

fun TextFieldData.toTextFieldUi(@StringRes errorResId: Int): TextFieldUi {
    return TextFieldUi(
        value = value,
        isError = isError,
        errorResId = errorResId,
    )
}

fun CategoriesFieldData.toCardFieldUi(): CardFieldUi {
    return CardFieldUi(
        labelResId = R.string.hint_common_menu_product_categories,
        value = selectedCategoryList
            .takeIf { list ->
                list.isNotEmpty()
            }?.joinToString(" ${Constants.BULLET_SYMBOL} ") { selectableCategory ->
                selectableCategory.category.name
            },
        isError = isError,
        errorResId = R.string.error_common_menu_product_categories
    )
}