package com.bunbeauty.fooddeliveryadmin.extensions

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bunbeauty.fooddeliveryadmin.R

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

fun ImageView.setImage(photoLink: String) {

    /*Picasso.get()
        .load(photoLink)
        .fit()
        .placeholder(R.drawable.default_product)
        .networkPolicy(NetworkPolicy.NO_CACHE)
        .memoryPolicy(MemoryPolicy.NO_CACHE)
        .into(this)*/
}

fun ImageView.getBitmap(): Bitmap {
    return (this.drawable as BitmapDrawable).bitmap
}