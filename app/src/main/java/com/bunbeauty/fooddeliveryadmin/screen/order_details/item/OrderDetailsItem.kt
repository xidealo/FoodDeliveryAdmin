package com.bunbeauty.fooddeliveryadmin.screen.order_details.item

import androidx.core.view.isGone
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.ListItem
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderDetailsBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer

data class OrderDetailsItem(
    val uuid: String,
    val phone: String,
    val time: String,
    val receiveMethod: String,
    val deferredTime: String?,
    val address: String?,
    val comment: String?,
    val status: String
) : ListItem() {

    override fun isTheSameField(other: ListItem) =
        (other is OrderDetailsItem) && (other.uuid == uuid)

    override fun isTheSameContent(other: ListItem) = (other == this)

    override fun getChangePayload(other: ListItem): Any? {
        other as OrderDetailsItem
        if (other.status != status) {
            return other.status
        }

        return null
    }
}

fun getOrderDetailsDelegate(onStatusClickListener: () -> Unit) =
    adapterDelegateLayoutContainer<OrderDetailsItem, ListItem>(
        R.layout.element_order_details
    ) {
        val binding = ElementOrderDetailsBinding.bind(itemView)
        binding.statusMcv.setOnClickListener {
            onStatusClickListener()
        }

        bind { payload ->
            val status = payload.firstOrNull() as? String
            if (status != null) {
                binding.statusValueTv.text = status
                return@bind
            }

            binding.apply {
                phoneValueTv.text = item.phone
                timeValueTv.text = item.time
                receiveMethodValueTv.text = item.receiveMethod

                deferredTimeTv.isGone = item.deferredTime.isNullOrEmpty()
                deferredTimeValueTv.isGone = item.deferredTime.isNullOrEmpty()
                deferredTimeValueTv.text = item.deferredTime

                addressTv.isGone = item.address.isNullOrEmpty()
                addressValueTv.isGone = item.address.isNullOrEmpty()
                addressValueTv.text = item.address

                commentTv.isGone = item.comment.isNullOrEmpty()
                commentValueTv.isGone = item.comment.isNullOrEmpty()
                commentValueTv.text = item.comment

                statusValueTv.text = item.status
            }
        }
    }

