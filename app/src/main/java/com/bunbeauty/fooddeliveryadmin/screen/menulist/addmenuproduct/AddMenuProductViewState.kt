package com.bunbeauty.fooddeliveryadmin.screen.menulist.addmenuproduct

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Stable
data class AddMenuProductViewState(
    val name: String,
    @StringRes val nameError: Int?,
    val newPrice: String,
    @StringRes val newPriceError: Int?,
    val oldPrice: String,
    val description: String,
    @StringRes val descriptionError: Int?,
    val nutrition: String,
    val comboDescription: String,
    val isLoadingButton: Boolean,
    val throwable: Throwable?,
) : BaseViewState