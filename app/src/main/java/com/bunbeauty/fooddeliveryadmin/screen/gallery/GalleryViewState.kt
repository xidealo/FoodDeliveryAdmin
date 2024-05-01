package com.bunbeauty.fooddeliveryadmin.screen.gallery

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class GalleryViewState(
    val photos: List<String>
) : BaseViewState
