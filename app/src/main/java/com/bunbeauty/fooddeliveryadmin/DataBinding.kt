package com.bunbeauty.fooddeliveryadmin

import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.data.model.BaseDiffUtilModel
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.ui.adapter.BaseAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

object DataBinding {

    @JvmStatic
    @BindingAdapter("bind:items")
    fun <T : BaseDiffUtilModel> setListItems(recyclerView: RecyclerView, items: List<T>?) {
        if (items != null && recyclerView.adapter != null) {
            (recyclerView.adapter as BaseAdapter<out RecyclerView.ViewHolder, T>).setItemList(items)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:addItemToTop")
    fun <T : BaseDiffUtilModel> addItemToTop(recyclerView: RecyclerView, item: T?) {
        if (item != null && recyclerView.adapter != null) {
            (recyclerView.adapter as BaseAdapter<out RecyclerView.ViewHolder, T>).addItemToTop(item)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:replaceItem")
    fun <T : BaseDiffUtilModel> replaceItem(recyclerView: RecyclerView, item: T?) {
        if (item != null && recyclerView.adapter != null) {
            (recyclerView.adapter as BaseAdapter<out RecyclerView.ViewHolder, T>).replaceItem(item)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:image")
    fun setImage(imageView: ImageView, link: String?) {
        if (!link.isNullOrEmpty()) {
            Picasso.get()
                .load(link)
                .placeholder(R.drawable.default_product)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView)
        }
    }
}