package com.bunbeauty.fooddeliveryadmin.ui.items

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.bunbeauty.fooddeliveryadmin.R
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.parcelize.IgnoredOnParcel
import java.lang.ref.SoftReference

abstract class ImageItem<B : ViewBinding> : AbstractBindingItem<B>() {

    private var loadedBitmap: SoftReference<Bitmap?> = SoftReference(null)

    fun setImage(imageView: ImageView, photoLink: String) {
        if (loadedBitmap.get() == null) {
            val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        loadedBitmap = SoftReference(bitmap)
                        imageView.setImageBitmap(bitmap)
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    imageView.setImageDrawable(placeHolderDrawable)
                }
            }
            imageView.tag = target
            Picasso.get()
                .load(photoLink)
                .placeholder(R.drawable.default_product)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(target)
        } else {
            imageView.setImageBitmap(loadedBitmap.get())
        }
    }

}