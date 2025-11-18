package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct

import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.screen.image.ImageFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.CardFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CreateMenuProductViewState(
    val nameField: TextFieldUi,
    val newPriceField: TextFieldUi,
    val oldPriceField: TextFieldUi,
    val nutritionField: TextFieldUi,
    val utils: String,
    val descriptionField: TextFieldUi,
    val comboDescription: String,
    val categoriesField: CardFieldUi,
    val isVisibleInMenu: Boolean,
    val isVisibleInRecommendation: Boolean,
    val imageField: ImageFieldUi,
    val sendingToServer: Boolean,
) : BaseViewState
