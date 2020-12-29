package com.bunbeauty.fooddeliveryadmin

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.fooddeliveryadmin.data.model.BaseModel
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.ui.adapter.BaseAdapter
import com.bunbeauty.fooddeliveryadmin.ui.view.ProgressButton
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

object DataBinding {

    @JvmStatic
    @BindingAdapter("android:text")
    fun setInt(textView: TextView, intValue: Int) {
        textView.text = intValue.toString()
    }

    @JvmStatic
    @BindingAdapter("bind:isLoading")
    fun setLoading(progressButton: ProgressButton, isLoading: Boolean) {
        if (isLoading) {
            progressButton.showLoading()
        } else {
            progressButton.hideLoading()
        }
    }

    @JvmStatic
    @BindingAdapter("bind:items")
    fun <T : BaseModel> setListItems(recyclerView: RecyclerView, items: List<T>?) {
        if (items != null && recyclerView.adapter != null) {
            (recyclerView.adapter as BaseAdapter<out RecyclerView.ViewHolder, T>).setItemList(items)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:removeItem")
    fun <T : BaseModel> removeItem(recyclerView: RecyclerView, item: T?) {
        if (item != null && recyclerView.adapter != null) {
            (recyclerView.adapter as BaseAdapter<out RecyclerView.ViewHolder, T>).removeItem(item)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:addItem")
    fun <T : BaseModel> addItem(recyclerView: RecyclerView, item: T?) {
        if (item != null && recyclerView.adapter != null) {
            (recyclerView.adapter as BaseAdapter<out RecyclerView.ViewHolder, T>).addItem(item)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:isLoading")
    fun setLoading(progressBar: ProgressBar, isLoading: Boolean) {
        if (isLoading) {
            progressBar.visible()
        } else {
            progressBar.gone()
        }
    }


    @JvmStatic
    @BindingAdapter("bind:leftIcon")
    fun setLeftIcon(textView: TextView, iconId: Int) {
        textView.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0)
    }

    @JvmStatic
    @BindingAdapter("bind:image")
    fun setImage(imageView: ImageView, link: String?) {
        if (!link.isNullOrEmpty()) {
            Picasso.get().load(link).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("bind:error")
    fun setError(textInputLayout: TextInputLayout, errorText: String?) {
        if (errorText == null || errorText.isEmpty()) {
            return
        }
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = errorText
    }
}