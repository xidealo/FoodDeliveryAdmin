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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import kotlin.math.roundToInt

@Composable
fun <T> DragDropList(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    key: ((item: T) -> Any)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (T) -> Unit,
    onItemReorder: (fromIndex: Int, toIndex: Int) -> Unit
) {
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var fromIndex by remember { mutableIntStateOf(0) }
    var toIndex by remember { mutableIntStateOf(0) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var itemHeight by remember { mutableFloatStateOf(0f) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        headerContent?.let {
            item(key = "header") {
                it()
            }
        }

        itemsIndexed(
            items = items,
            key = key?.let { k -> { index, item -> k(item) } } ?: { index, item -> index }
        ) { index, item ->
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
                DragDropListItem(
                    item = item,
                    isDragging = isDragging,
                    itemContent = itemContent,
                    onDragStart = {
                        draggingIndex = index
                        dragOffset = Offset.Zero
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount
                        fromIndex = draggingIndex ?: return@DragDropListItem

                        val delta = (dragOffset.y / itemHeight).roundToInt()
                        toIndex = (fromIndex + delta).coerceIn(0, items.lastIndex)
                    },
                    onDragEnd = {
                        if (fromIndex != toIndex && toIndex in items.indices) {
                            onItemReorder(fromIndex, toIndex)
                        }
                        dragOffset = Offset.Zero
                        draggingIndex = null
                    },
                    onDragCancel = {
                        dragOffset = Offset.Zero
                        draggingIndex = null
                    },
                    onHeightMeasured = { height ->
                        itemHeight = height
                    }
                )
            }
        }
    }
}

@Composable
private fun <T> DragDropListItem(
    item: T,
    isDragging: Boolean,
    itemContent: @Composable (T) -> Unit,
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
            itemContent(item)
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