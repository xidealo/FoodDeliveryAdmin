package com.bunbeauty.fooddeliveryadmin.util

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

fun TextView.strikeOutText() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun ImageView.getBitmap(): Bitmap = (this.drawable as BitmapDrawable).bitmap

fun View.setLinearLayoutMargins(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0,
) {
    layoutParams =
        (layoutParams as LinearLayout.LayoutParams).apply {
            setMargins(left, top, right, bottom)
        }
}

fun RecyclerView.addSpaceItemDecorator(
    @DimenRes spaceId: Int,
) {
    addItemDecoration(
        object : DividerItemDecoration(context, LinearLayout.VERTICAL) {
            init {
                val space =
                    ShapeDrawable().apply {
                        intrinsicHeight = resources.getDimensionPixelOffset(spaceId)
                        paint.color = Color.TRANSPARENT
                    }
                setDrawable(space)
            }

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position == state.itemCount - 1) {
                    outRect.setEmpty()
                } else {
                    super.getItemOffsets(outRect, view, parent, state)
                }
            }
        },
    )
}

inline fun ComposeView.compose(crossinline content: @Composable () -> Unit) {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        AdminTheme {
            content()
        }
    }
}
