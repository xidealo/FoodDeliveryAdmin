package com.bunbeauty.fooddeliveryadmin.ui.adapter.items

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.room.Ignore
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.data.model.cart_product.CartProduct
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderUI
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val status: OrderStatus,
    val code: String,
    val deferredTime: String,
    val time: String,
    val order: Order,
) : AbstractBindingItem<ElementOrderBinding>(), Parcelable {

    @IgnoredOnParcel
    @Ignore
    override val type = R.id.element_text_tv_title

    override fun bindView(binding: ElementOrderBinding, payloads: List<Any>) {
        binding.elementOrderMvcMain.background.setTint(
            ContextCompat.getColor(binding.root.context, getBackgroundColor(status))
        )
        binding.elementOrderTvCode.text = code
        binding.elementOrderTvDeferred.text = deferredTime
        binding.elementOrderTvTime.text = time
        binding.elementOrderIvStatus.background =
            ContextCompat.getDrawable(binding.root.context, getIcon(status))
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
            CANCELED -> R.color.canceledColor
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