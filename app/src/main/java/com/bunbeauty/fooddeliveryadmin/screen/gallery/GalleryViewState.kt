package com.bunbeauty.fooddeliveryadmin.screen.gallery

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class GalleryViewState(
    val photos: ImmutableList<String>,
    val isLoading: Boolean,
    val hasError: Boolean
) : BaseViewState
