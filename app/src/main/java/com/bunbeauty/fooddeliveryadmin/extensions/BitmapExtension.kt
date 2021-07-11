package com.bunbeauty.fooddeliveryadmin.extensions

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this@toByteArray.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}