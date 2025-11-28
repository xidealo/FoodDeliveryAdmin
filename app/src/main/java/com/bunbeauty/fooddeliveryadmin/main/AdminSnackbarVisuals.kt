package com.bunbeauty.fooddeliveryadmin.main

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.bunbeauty.presentation.viewmodel.main.Main

data class AdminSnackbarVisuals(
    val adminMessage: Main.Message,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals {
    override val message: String = adminMessage.text
}
