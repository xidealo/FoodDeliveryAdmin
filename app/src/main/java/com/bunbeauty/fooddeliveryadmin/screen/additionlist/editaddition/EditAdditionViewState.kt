package com.bunbeauty.fooddeliveryadmin.screen.additionlist.editaddition

import androidx.annotation.StringRes
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class EditAdditionViewState(
    val name: String,
    @StringRes val editNameError: Int?,
    val priority: String,
    val fullName: String,
    val price: String,
    @StringRes val editPriceError: Int?,
    val isVisible: Boolean,
    val isLoading: Boolean
) : BaseViewState
