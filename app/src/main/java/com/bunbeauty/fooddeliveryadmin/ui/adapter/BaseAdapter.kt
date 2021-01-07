package com.bunbeauty.fooddeliveryadmin.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.fooddeliveryadmin.data.model.BaseModel

abstract class BaseAdapter<T : RecyclerView.ViewHolder, E : BaseModel> : RecyclerView.Adapter<T>() {

    protected val itemList: MutableList<E> = ArrayList()

    open fun setItemList(items: List<E>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(items, itemList))
        itemList.clear()
        itemList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    open fun removeItem(item: E) {
        val index = itemList.indexOf(item)
        itemList.remove(item)
        notifyItemRemoved(index)
    }

    open fun addItem(item: E) {
        itemList.add(item)
        notifyItemInserted(itemCount)
    }

    open fun addItemToTop(item: E) {
        itemList.add(0, item)
        notifyItemInserted(0)
    }

    open fun replaceItem(item: E){
        val foundedItem = itemList.find { it.uuid == item.uuid } ?: return
        val index = itemList.indexOf(foundedItem)
        itemList[index] = item
        notifyItemChanged(index)
    }

    override fun getItemCount() = itemList.size
}