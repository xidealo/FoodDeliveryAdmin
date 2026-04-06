package com.bunbeauty.presentation.feature.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): () -> Unit =
    remember {
        { }
    }
