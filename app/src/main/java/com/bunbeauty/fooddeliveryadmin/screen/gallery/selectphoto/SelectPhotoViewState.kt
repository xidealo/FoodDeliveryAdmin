package com.bunbeauty.fooddeliveryadmin.screen.gallery.selectphoto

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class SelectPhotoViewState(
    val photoLink: String?,
    val isLoading: Boolean,
    val hasError: Boolean
) : BaseViewState
