package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class AddMenuProductViewState(
    val name: String,
    @StringRes val nameError: Int?,
    val newPrice: String,
    @StringRes val newPriceError: Int?,
    val oldPrice: String,
    @StringRes val oldPriceError: Int?,
    val utils: String,
    val description: String,
    @StringRes val descriptionError: Int?,
    val nutrition: String,
    val comboDescription: String,
    val categoryLabel: String,
    @StringRes val categoryHint: Int?,
    val isVisibleInMenu: Boolean,
    val isVisibleInRecommendation: Boolean,
    val categoriesBorder: BorderStroke?,
    val selectableCategoryList: List<CategoryItem>,
    val imageUris: ImageUris?,
    val imageError: Boolean,
    val sendingToServer: Boolean,
) : BaseViewState {

    @Immutable
    data class ImageUris(
        val originalImageUri: Uri,
        val croppedImageData: ImageData.LocalUri
    )

    @Immutable
    data class CategoryItem(
        val uuid: String,
        val name: String,
        val selected: Boolean
    )
}
