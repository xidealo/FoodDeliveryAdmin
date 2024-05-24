package com.bunbeauty.fooddeliveryadmin.screen.gallery.selectphoto

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class SelectPhotoViewState(
    val photo: String?,
    val isLoading: Boolean,
    val hasError: Boolean
) : BaseViewState
