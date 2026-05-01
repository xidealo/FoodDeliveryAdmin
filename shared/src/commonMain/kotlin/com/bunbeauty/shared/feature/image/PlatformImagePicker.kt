package com.bunbeauty.shared.feature.image

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): () -> Unit
