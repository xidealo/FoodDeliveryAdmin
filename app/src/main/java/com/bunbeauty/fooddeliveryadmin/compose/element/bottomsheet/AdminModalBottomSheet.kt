package com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold

// WARNING: Don't use until M3 fixes bottom insets bug
// https://issuetracker.google.com/issues/274872542

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminModalBottomSheet(
    modifier: Modifier = Modifier,
    title: String,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
    containerColor: Color = AdminTheme.colors.main.surface,
    contentColor: Color = AdminTheme.colors.main.onSurface,
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        dragHandle = { DragHandle() },
        windowInsets = WindowInsets(0, 0, 0, 0),
        content = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = title,
                style = AdminTheme.typography.titleMedium.bold,
                color = AdminTheme.colors.main.onSurface,
                textAlign = TextAlign.Center
            )
            content()
            Spacer(modifier = Modifier.height(16.dp))
        }
    )
}

@Composable
private fun DragHandle() {
    Spacer(
        modifier = Modifier
            .padding(top = 8.dp)
            .width(32.dp)
            .height(4.dp)
            .background(
                color = AdminTheme.colors.main.onSurfaceVariant,
                shape = RoundedCornerShape(2.dp)
            )
    )
}