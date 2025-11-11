package com.bunbeauty.fooddeliveryadmin.coreui

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue

interface Customizable {
    fun getString(
        context: Context,
        attributeSet: AttributeSet?,
        attributeId: Int,
        styleableId: IntArray,
        defaultString: String,
    ): String =
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, styleableId)
            val text = typedArray.getString(attributeId) ?: defaultString
            typedArray.recycle()

            text
        } else {
            defaultString
        }

    fun getDimensionPixel(
        context: Context,
        attributeSet: AttributeSet?,
        attributeId: Int,
        styleableId: IntArray,
        defaultDip: Float,
    ): Int =
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, styleableId)
            val dimensionPixel =
                typedArray.getLayoutDimension(
                    attributeId,
                    getPixels(defaultDip, context.resources),
                )
            typedArray.recycle()

            dimensionPixel
        } else {
            getPixels(defaultDip, context.resources)
        }

    fun getInteger(
        context: Context,
        attributeSet: AttributeSet?,
        attributeId: Int,
        styleableId: IntArray,
        defaultInt: Int,
    ): Int =
        if (attributeSet != null) {
            val typedArray = context.obtainStyledAttributes(attributeSet, styleableId)
            val integerValue = typedArray.getInteger(attributeId, defaultInt)
            typedArray.recycle()

            integerValue
        } else {
            defaultInt
        }

    fun getColor(
        context: Context,
        attributeSet: AttributeSet?,
        attributeId: Int,
        styleableId: IntArray,
        defaultColor: Int,
    ): Int =
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, styleableId)
            val color = typedArray.getColor(attributeId, defaultColor)
            typedArray.recycle()

            color
        } else {
            defaultColor
        }

    fun getDrawable(
        context: Context,
        attributeSet: AttributeSet?,
        attributeId: Int,
        styleableId: IntArray,
    ): Drawable? =
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, styleableId)
            val drawable = typedArray.getDrawable(attributeId)
            typedArray.recycle()

            drawable
        } else {
            null
        }

    fun getBoolean(
        context: Context,
        attributeSet: AttributeSet?,
        attributeId: Int,
        styleableId: IntArray,
        default: Boolean,
    ): Boolean =
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, styleableId)
            val boolean = typedArray.getBoolean(attributeId, default)
            typedArray.recycle()

            boolean
        } else {
            default
        }

    private fun getPixels(
        dip: Float,
        resources: Resources,
    ): Int =
        TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
            .toInt()
}
