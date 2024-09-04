package com.bunbeauty.fooddeliveryadmin.screen.additionlist.createaddition

import androidx.annotation.StringRes
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class CreateAdditionViewState(
    val name: String,
    @StringRes
    val createNameError: Int?,
    val priority: String,
    val fullName: String,
    val price: String,
    @StringRes
    val createPriceError: Int?,
    val isVisible: Boolean,
    val isLoading: Boolean
) : BaseViewState