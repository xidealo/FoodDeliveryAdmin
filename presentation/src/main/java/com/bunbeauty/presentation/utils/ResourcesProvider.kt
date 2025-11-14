package com.bunbeauty.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class ResourcesProvider(
    private val context: Context,
) : IResourcesProvider {
    override fun getString(stringId: Int): String = context.resources.getString(stringId)

    override fun getDrawable(drawableId: Int): Drawable? = ContextCompat.getDrawable(context, drawableId)

    override fun getColor(colorId: Int): Int = ContextCompat.getColor(context, colorId)

    override fun getDimension(dimensionId: Int): Int = context.resources.getDimensionPixelSize(dimensionId)

    override fun getDimensionFloat(dimensionId: Int): Float = getDimension(dimensionId).toFloat()
}
