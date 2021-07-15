package com.bunbeauty.fooddeliveryadmin.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import com.bunbeauty.presentation.model.OrderItemModel
import com.mikepenz.fastadapter.binding.AbstractBindingItem

data class OrderItem(
    val orderItemModel: OrderItemModel
) : AbstractBindingItem<ElementOrderBinding>() {

    override val type = R.id.element_order_mvc_main

    override fun bindView(binding: ElementOrderBinding, payloads: List<Any>) {
        binding.run {
            elementOrderTvCode.text = orderItemModel.code
            elementOrderTvDeferred.text = orderItemModel.deferredTime
            elementOrderTvTime.text = orderItemModel.time
            elementOrderChipStatus.text = orderItemModel.statusString
            elementOrderChipStatus.setChipBackgroundColorResource(getBackgroundColor(orderItemModel.status))
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementOrderBinding {
        return ElementOrderBinding.inflate(inflater, parent, false)
    }

    private fun getBackgroundColor(status: OrderStatus): Int {
        return when (status) {
            NOT_ACCEPTED -> R.color.notAcceptedColor
            ACCEPTED -> R.color.acceptedColor
            PREPARING -> R.color.preparingColor
            SENT_OUT -> R.color.sentOutColor
            DONE -> R.color.doneColor
            DELIVERED -> R.color.deliveredColor
            else -> R.color.notAcceptedColor
        }
    }
}