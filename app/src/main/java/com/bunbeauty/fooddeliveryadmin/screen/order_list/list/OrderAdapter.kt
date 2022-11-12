package com.bunbeauty.fooddeliveryadmin.screen.order_list.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import javax.inject.Inject

class OrderAdapter @Inject constructor(
    orderDiffUtilItemCallback: OrderDiffUtilItemCallback
) : ListAdapter<OrderItemModel, OrderAdapter.OrderViewHolder>(orderDiffUtilItemCallback) {

    var onClickListener: ((OrderItemModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementOrderBinding.inflate(
            inflater,
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(getItem(position))
        }
    }

    inner class OrderViewHolder(val binding: ElementOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(orderItemModel: OrderItemModel) {
            binding.apply {
                codeTv.text = orderItemModel.code
                statusChip.text = orderItemModel.statusString
                statusChip.setChipBackgroundColorResource(getStatusColor(orderItemModel.status))
                timeTv.text = orderItemModel.time
                deferredTv.text = orderItemModel.deferredTime
            }
        }

        private fun getStatusColor(status: OrderStatus): Int {
            return when (status) {
                OrderStatus.NOT_ACCEPTED -> R.color.notAcceptedColor
                OrderStatus.ACCEPTED -> R.color.acceptedColor
                OrderStatus.PREPARING -> R.color.preparingColor
                OrderStatus.SENT_OUT -> R.color.sentOutColor
                OrderStatus.DONE -> R.color.doneColor
                OrderStatus.DELIVERED -> R.color.deliveredColor
                OrderStatus.CANCELED -> 0
            }
        }
    }
}