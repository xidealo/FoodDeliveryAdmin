package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.room.Ignore
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.CartProduct
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
    val isDelivery: Boolean,
    val comment: String,
    val email: String,
    val phone: String,
    val address: String,
    val cartProductList: List<CartProduct>,
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
        binding.elementOrderTvCode.text = code
        binding.elementOrderTvDeferred.text = deferredTime
        binding.elementOrderTvTime.text = time
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
        return when (status) {
            OrderStatus.NOT_ACCEPTED -> R.color.notAcceptedColor
            OrderStatus.ACCEPTED -> R.color.notAcceptedColor
            OrderStatus.PREPARING -> R.color.preparingColor
            OrderStatus.SENT_OUT -> R.color.sentOutColor
            OrderStatus.DONE -> R.color.doneColor
            OrderStatus.CANCELED -> R.color.canceledColor
            else -> R.color.notAcceptedColor
        }
    }

    private fun getIcon(): Int {
        return when (status) {
            OrderStatus.NOT_ACCEPTED -> R.drawable.ic_not_accepted
            OrderStatus.ACCEPTED -> R.drawable.ic_accepted
            OrderStatus.PREPARING -> R.drawable.ic_preparing
            OrderStatus.SENT_OUT -> R.drawable.ic_sent_out
            OrderStatus.DONE -> R.drawable.ic_done
            OrderStatus.CANCELED -> R.drawable.ic_canceled
            else -> R.drawable.ic_not_accepted
        }
    }
}