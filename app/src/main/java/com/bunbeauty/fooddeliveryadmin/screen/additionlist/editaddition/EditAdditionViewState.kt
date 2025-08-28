package com.bunbeauty.fooddeliveryadmin.screen.additionlist.editaddition

import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.screen.image.ImageFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class EditAdditionViewState(
    val nameField: TextFieldUi,
    val priorityField: TextFieldUi,
    val fullName: String,
    val price: String,
    val tag: String,
    val isVisible: Boolean,
    val isLoading: Boolean,
    val imageFieldUi: ImageFieldUi
) : BaseViewState
