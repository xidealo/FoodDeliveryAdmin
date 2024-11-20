package com.bunbeauty.fooddeliveryadmin.screen.additionlist.createaddition

import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CreateAdditionViewState(
    val name: TextFieldUi,
    val priority: TextFieldUi,
    val fullName: String?,
    val price: TextFieldUi,
    val isVisible: Boolean,
    val isLoading: Boolean
) : BaseViewState
