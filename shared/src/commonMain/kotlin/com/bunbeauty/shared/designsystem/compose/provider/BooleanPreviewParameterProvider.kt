package com.bunbeauty.shared.designsystem.compose.provider

import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}
