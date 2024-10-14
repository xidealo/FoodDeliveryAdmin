package com.bunbeauty.fooddeliveryadmin.screen.additionlist.editaddition

import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class EditAdditionViewState(
    val nameField: TextFieldUi,
    val priorityField: TextFieldUi,
    val fullName: String,
    val price: String,
    val isVisible: Boolean,
    val isLoading: Boolean
) : BaseViewState
