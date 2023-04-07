package com.bunbeauty.fooddeliveryadmin.screen.order_details.item

import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.ListItem
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderProductBinding
import com.bunbeauty.fooddeliveryadmin.util.strikeOutText
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer

data class OrderProductItem(
    val uuid: String,
    val name: String,
    val count: String,
    val oldCost: String,
    val newCost: String,
) : ListItem() {

    override fun isTheSameField(other: ListItem) =
        (other is OrderProductItem) && (other.uuid == uuid)

    override fun isTheSameContent(other: ListItem) = other == this
}

fun getOrderProductDelegate() = adapterDelegateLayoutContainer<OrderProductItem, ListItem>(
    R.layout.element_order_product
) {
    val binding = ElementOrderProductBinding.bind(itemView)

    bind {
        binding.apply {
            titleTv.text = item.name
            countTv.text = item.count
            oldCostTv.text = item.oldCost
            oldCostTv.strikeOutText()
            newCostTv.text = item.newCost
        }
    }
}
