package com.bunbeauty.presentation.utils

import android.graphics.drawable.Drawable

interface IResourcesProvider {
    @Deprecated("use resources")
    fun getString(stringId: Int): String
    fun getDrawable(drawableId: Int): Drawable?
    fun getColor(colorId: Int): Int
    fun getDimension(dimensionId: Int): Int
    fun getDimensionFloat(dimensionId: Int): Float
}