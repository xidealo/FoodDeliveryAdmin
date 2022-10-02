package com.bunbeauty.fooddeliveryadmin.util

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

fun View.invisible(): View {
    visibility = View.INVISIBLE
    return this
}

fun View.visible(): View {
    visibility = View.VISIBLE
    return this
}

fun View.gone(): View {
    visibility = View.GONE
    return this
}

fun View.toggleVisibility(isVisible: Boolean): View {
    if (isVisible) {
        this.visible()
    } else {
        this.gone()
    }
    return this
}

fun TextView.strikeOutText() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun ImageView.getBitmap(): Bitmap {
    return (this.drawable as BitmapDrawable).bitmap
}

fun View.setLinearLayoutMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}