package com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminModalBottomSheet(
    onDismissRequest: () -> Unit,
    isShown: Boolean,
    modifier: Modifier = Modifier,
    title: String? = null,
    contentPadding: PaddingValues =
        PaddingValues(
            top = 8.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
        ),
    shape: Shape = AdminBottomSheetDefaults.shape,
    containerColor: Color = AdminTheme.colors.main.surface,
    contentColor: Color = contentColorFor(containerColor),
    dragHandle: @Composable (() -> Unit)? = { AdminBottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    content: @Composable ColumnScope.() -> Unit,
    density: Density = LocalDensity.current,
) {
    var isVisible by remember {
        mutableStateOf(false)
    }
    val sheetState =
        remember {
            SheetState(
                skipPartiallyExpanded = true,
                initialValue = SheetValue.Hidden,
                positionalThreshold = { with(density) { 56.dp.toPx() } },
                velocityThreshold = { with(density) { 125.dp.toPx() } },
            )
        }

    LaunchedEffect(isShown) {
        if (!isShown) {
            sheetState.hide()
        }

        isVisible = isShown
    }

    if (isVisible) {
        val systemBottomBarHeight = getSystemBottomBarHeight()
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            dragHandle = dragHandle,
            contentWindowInsets = contentWindowInsets,
            modifier = modifier,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(paddingValues = contentPadding)
                        .padding(bottom = systemBottomBarHeight),
            ) {
                title?.let {
                    Title(
                        modifier = Modifier.padding(vertical = 16.dp),
                        title = title,
                    )
                }
                content()
            }
        }
    }
}

@Composable
private fun getSystemBottomBarHeight(): Dp {
    val view = LocalView.current
    val density = LocalDensity.current

    return remember {
        val insets = ViewCompat.getRootWindowInsets(view)
        if (insets != null) {
            val bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            with(density) {
                bottomInset.toDp().value.dp
            }
        } else {
            0.dp
        }
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = AdminTheme.typography.titleMedium.bold,
        color = AdminTheme.colors.main.onSurface,
        textAlign = TextAlign.Center,
    )
}

@Preview
@Composable
private fun AdminModalBottomSheetPreview() {
    var isShownState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(100)
        isShownState = true
    }
    AdminTheme {
        AdminModalBottomSheet(
            onDismissRequest = {},
            isShown = isShownState,
            content = {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
                ) {
                    repeat(4) {
                        Spacer(
                            modifier =
                                Modifier
                                    .height(40.dp)
                                    .fillMaxWidth()
                                    .background(AdminTheme.colors.main.surface),
                        )
                    }
                }
            },
        )
    }
}
