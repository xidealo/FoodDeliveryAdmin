package com.bunbeauty.fooddeliveryadmin.core_ui

import androidx.recyclerview.widget.DiffUtil

abstract class ListItem {
    abstract fun isTheSameField(other: ListItem): Boolean
    abstract fun isTheSameContent(other: ListItem): Boolean
    open fun getChangePayload(other: ListItem): Any? = null
}

val FIELD_ITEM_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) =
        oldItem.isTheSameField(newItem)

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) =
        oldItem.isTheSameContent(newItem)

    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? =
        oldItem.getChangePayload(newItem)
}
