package com.bunbeauty.fooddeliveryadmin.screen.additionlist.editaddition

import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class EditAdditionViewState(
    val name: String,
    val hasNameError: Boolean,
    val priority: String,
    val fullName: String,
    val hasFullNameError: Boolean,
    val price: String,
    val isVisible: Boolean,
    val isLoading: Boolean,
    val error: Throwable?
) : BaseViewState


