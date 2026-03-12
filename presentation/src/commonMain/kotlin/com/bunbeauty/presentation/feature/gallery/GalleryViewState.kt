package com.bunbeauty.presentation.feature.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class GalleryViewState(
    val photos: ImmutableList<String>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val hasError: Boolean,
) : BaseViewState

@Composable
internal fun Gallery.DataState.toViewState(): GalleryViewState =
    GalleryViewState(
        photos = photoList.map { photo -> photo.url }.toPersistentList(),
        isLoading = isLoading,
        hasError = hasError,
        isRefreshing = isRefreshing,
    )
