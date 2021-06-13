package com.bunbeauty.domain.resources

import android.graphics.drawable.Drawable

interface IResourcesProvider {
    fun getString(stringId: Int): String
    fun getDrawable(drawableId: Int): Drawable?
    fun getColor(colorId: Int): Int
    fun getDimension(dimensionId: Int): Int
    fun getDimensionFloat(dimensionId: Int): Float
}