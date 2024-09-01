package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.FieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.CardFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CreateMenuProductViewState(
    val nameField: TextFieldUi,
    val newPriceField: TextFieldUi,
    val oldPriceField: TextFieldUi,
    val nutrition: String,
    val utils: String,
    val descriptionField: TextFieldUi,
    val comboDescription: String,
    val categoriesField: CardFieldUi,
    val isVisibleInMenu: Boolean,
    val isVisibleInRecommendation: Boolean,
    val imageField: ImageFieldUi,
    val sendingToServer: Boolean,
) : BaseViewState {

    @Immutable
    data class ImageFieldUi(
        override val value: ImageData.LocalUri?,
        override val isError: Boolean
    ): FieldUi<ImageData.LocalUri?>() {
        val isSelected: Boolean = value != null
    }
}


