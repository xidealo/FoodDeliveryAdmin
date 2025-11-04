package com.bunbeauty.fooddeliveryadmin.compose.element

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import kotlin.math.roundToInt

@Composable
fun <T> DragDropList(
    title: String? = null,
    items: List<T>,
    itemKey: (T) -> Any,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    itemLabel: (T) -> String
) {
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var fromIndex by remember { mutableIntStateOf(0) }
    var toIndex by remember { mutableIntStateOf(0) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var itemHeight by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(items) {
        draggingIndex = null
        fromIndex = 0
        toIndex = 0
        dragOffset = Offset.Zero
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = AdminTheme.dimensions.scrollScreenBottomSpace
        )
    ) {
        title?.takeIf { titleItems -> titleItems.isNotBlank() }?.let { nonEmptyTitle ->
            item(key = "title_key") {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .animateItem(),
                    text = nonEmptyTitle,
                    style = AdminTheme.typography.titleMedium.bold
                )
            }
        }

        itemsIndexed(items, key = { index, item -> "${itemKey(item)}-$index" }) { index, item ->
            val isDragging = draggingIndex == index

            val offsetModifier = if (isDragging) {
                Modifier
                    .offset { IntOffset(0, dragOffset.y.roundToInt()) }
                    .zIndex(1f)
            } else {
                Modifier
            }

            Box(
                modifier = Modifier
                    .then(offsetModifier)
                    .fillMaxWidth()
                    .zIndex(0.9f)
                    .animateItem()
            ) {
                DraggableListItem(
                    text = itemLabel(item),
                    onDragStart = {
                        draggingIndex = items.indexOfFirst { itemKey(it) == itemKey(item) }
                        fromIndex = draggingIndex ?: 0
                        dragOffset = Offset.Zero
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount

                        val startIndex = draggingIndex ?: return@DraggableListItem
                        val offsetItems = (dragOffset.y / itemHeight).roundToInt()
                        val potentialIndex = (startIndex + offsetItems)
                            .coerceIn(0, items.lastIndex)

                        if (toIndex != potentialIndex) {
                            toIndex = potentialIndex
                        }
                    },

                    onDragEnd = {
                        if (fromIndex != toIndex && toIndex in items.indices) {
                            onMove(fromIndex, toIndex)
                        }
                        draggingIndex = null
                        dragOffset = Offset.Zero
                    },
                    onDragCancel = {
                        dragOffset = Offset.Zero
                    },
                    onHeightMeasured = { height -> itemHeight = height }
                )
            }
        }
    }
}

@Composable
private fun DraggableListItem(
    text: String,
    onDragStart: (Offset) -> Unit,
    onDrag: (PointerInputChange, Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    onHeightMeasured: (Float) -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .onGloballyPositioned { coordinates ->
            onHeightMeasured(coordinates.size.height.toFloat())
        }
    AdminCard(
        modifier = modifier
            .padding(vertical = 4.dp),
        shape = noCornerCardShape,
        elevated = false
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_drad_handle),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = onDragStart,
                            onDragEnd = onDragEnd,
                            onDragCancel = onDragCancel,
                            onDrag = onDrag
                        )
                    }
            )
        }
    }
}
