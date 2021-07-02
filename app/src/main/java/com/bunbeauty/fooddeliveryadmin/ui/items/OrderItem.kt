package com.bunbeauty.fooddeliveryadmin.ui.items

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val status: OrderStatus,
    val statusString: String,
    val code: String,
    val deferredTime: String,
    val time: String,
    val order: Order,
) : AbstractBindingItem<ElementOrderBinding>(), Parcelable {

    @IgnoredOnParcel
    override val type = R.id.element_text_tv_title

    override fun bindView(binding: ElementOrderBinding, payloads: List<Any>) {
        binding.elementOrderTvCode.text = code
        binding.elementOrderTvDeferred.text = deferredTime
        binding.elementOrderTvTime.text = time
        binding.elementOrderChipStatus.text = statusString
        binding.elementOrderChipStatus.setChipBackgroundColorResource(getBackgroundColor(status))
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

    private fun getIcon(status: OrderStatus): Int {
        return when (status) {
            NOT_ACCEPTED -> R.drawable.ic_not_accepted
            ACCEPTED -> R.drawable.ic_accepted
            PREPARING -> R.drawable.ic_preparing
            SENT_OUT -> R.drawable.ic_sent_out
            DONE -> R.drawable.ic_done
            CANCELED -> R.drawable.ic_canceled
            else -> R.drawable.ic_not_accepted
        }
    }
}