package com.bunbeauty.fooddeliveryadmin.screen.menu

import androidx.compose.runtime.Stable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Stable
data class MenuViewState(
    val error: Throwable?
) : BaseViewState
