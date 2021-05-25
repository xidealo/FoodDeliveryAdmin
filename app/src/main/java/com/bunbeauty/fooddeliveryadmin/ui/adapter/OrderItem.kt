package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.room.Ignore
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.data.model.order.OrderUI
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val orderUI: OrderUI
) : AbstractBindingItem<ElementOrderBinding>(), Parcelable {

    @IgnoredOnParcel
    @Ignore
    override val type = R.id.element_text_mcv_main

    override fun bindView(binding: ElementOrderBinding, payloads: List<Any>) {
        binding.elementOrderMvcMain.setBackgroundColor(
            ContextCompat.getColor(
                binding.elementOrderMvcMain.context,
                getBackgroundColor()
            )
        )
        binding.elementOrderTvCode.text = orderUI.code
        binding.elementOrderTvDeferred.text = orderUI.deferredTime
        binding.elementOrderTvTime.text = orderUI.time
        binding.elementOrderIvStatus.background =
            ContextCompat.getDrawable(binding.elementOrderIvStatus.context, getIcon())
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementOrderBinding {
        return ElementOrderBinding.inflate(inflater, parent, false)
    }

    private fun getBackgroundColor(): Int {
        return when (orderUI.status) {
            NOT_ACCEPTED -> R.color.notAcceptedColor
            ACCEPTED -> R.color.acceptedColor
            PREPARING -> R.color.preparingColor
            SENT_OUT -> R.color.sentOutColor
            DONE -> R.color.doneColor
            CANCELED -> R.color.canceledColor
            else -> R.color.notAcceptedColor
        }
    }

    private fun getIcon(): Int {
        return when (orderUI.status) {
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