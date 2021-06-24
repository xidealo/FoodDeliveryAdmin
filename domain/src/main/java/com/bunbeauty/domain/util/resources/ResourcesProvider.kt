package com.bunbeauty.domain.util.resources

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourcesProvider @Inject constructor(@ApplicationContext private val context: Context) :
    IResourcesProvider {

    override fun getString(stringId: Int): String {
        return context.resources.getString(stringId)
    }

    override fun getDrawable(drawableId: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableId)
    }

    override fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    override fun getDimension(dimensionId: Int): Int {
        return context.resources.getDimensionPixelSize(dimensionId)
    }

    override fun getDimensionFloat(dimensionId: Int): Float {
        return getDimension(dimensionId).toFloat()
    }
}