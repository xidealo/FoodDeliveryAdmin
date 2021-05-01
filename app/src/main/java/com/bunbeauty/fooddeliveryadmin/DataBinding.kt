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
    @BindingAdapter("bind:status")
    fun setStatus(materialCardView: MaterialCardView, orderStatus: OrderStatus) {

        val backgroundColor = when (orderStatus) {
            OrderStatus.NOT_ACCEPTED -> R.color.notAcceptedColor
            OrderStatus.ACCEPTED -> R.color.notAcceptedColor
            OrderStatus.PREPARING -> R.color.preparingColor
            OrderStatus.SENT_OUT -> R.color.sentOutColor
            OrderStatus.DONE -> R.color.deliveredColor
        }
        materialCardView.setBackgroundColor(
            ContextCompat.getColor(
                materialCardView.context,
                backgroundColor
            )
        )
    }

    @JvmStatic
    @BindingAdapter("bind:status")
    fun setStatus(imageView: ImageView, orderStatus: OrderStatus) {

        val backgroundIcon = when (orderStatus) {
            OrderStatus.NOT_ACCEPTED -> R.drawable.ic_not_accepted
            OrderStatus.ACCEPTED -> R.drawable.ic_accepted
            OrderStatus.PREPARING -> R.drawable.ic_preparing
            OrderStatus.SENT_OUT -> R.drawable.ic_sent_out
            OrderStatus.DONE -> R.drawable.ic_done
        }
        imageView.background = ContextCompat.getDrawable(imageView.context, backgroundIcon)
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