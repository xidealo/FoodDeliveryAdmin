package com.bunbeauty.presentation.feature.image

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(
    onImagePicked: (String) -> Unit,
): () -> Unit
