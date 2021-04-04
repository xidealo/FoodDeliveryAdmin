package com.bunbeauty.fooddeliveryadmin.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bunbeauty.data.model.BaseDiffUtilModel

class MyDiffCallback(
    private val newList: List<BaseDiffUtilModel>,
    private val oldList: List<BaseDiffUtilModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].uuid == oldList[oldItemPosition].uuid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }
}