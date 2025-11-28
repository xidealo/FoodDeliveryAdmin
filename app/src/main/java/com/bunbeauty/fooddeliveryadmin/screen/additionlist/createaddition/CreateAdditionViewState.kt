package com.bunbeauty.fooddeliveryadmin.screen.additionlist.createaddition

import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.screen.image.ImageFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CreateAdditionViewState(
    val nameField: TextFieldUi,
    val fullName: String,
    val price: String,
    val tag: String,
    val isVisible: Boolean,
    val isLoading: Boolean,
    val imageFieldUi: ImageFieldUi
) : BaseViewState
