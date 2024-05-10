package com.bunbeauty.fooddeliveryadmin.screen.menulist.addmenuproduct

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
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
    val isLoadingButton: Boolean,
    val throwable: Throwable?,
    val categoryLabel: String,
    @StringRes val categoryHint: Int?,
    val isVisibleInMenu: Boolean,
    val isVisibleInRecommendation: Boolean
) : BaseViewState