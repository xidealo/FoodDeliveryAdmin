package com.bunbeauty.fooddeliveryadmin.screen.order_list.list

import androidx.recyclerview.widget.DiffUtil
import javax.inject.Inject

class OrderDiffUtilItemCallback @Inject constructor() : DiffUtil.ItemCallback<OrderItemModel>() {

    override fun areItemsTheSame(
        oldItem: OrderItemModel,
        newItem: OrderItemModel
    ): Boolean = oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(
        oldItem: OrderItemModel,
        newItem: OrderItemModel
    ): Boolean = oldItem == newItem
}
