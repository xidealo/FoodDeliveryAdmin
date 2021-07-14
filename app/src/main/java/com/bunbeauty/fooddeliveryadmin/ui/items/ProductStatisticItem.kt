package com.bunbeauty.fooddeliveryadmin.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementProductStatisticBinding
import com.bunbeauty.presentation.model.ProductStatisticItemModel
import com.mikepenz.fastadapter.binding.AbstractBindingItem

data class ProductStatisticItem(
    val productStatisticItemModel: ProductStatisticItemModel
) : AbstractBindingItem<ElementProductStatisticBinding>() {

    override val type = R.id.element_product_statistic_mcv_main

    override fun bindView(binding: ElementProductStatisticBinding, payloads: List<Any>) {
        with(binding) {
            elementProductStatisticTvName.text = productStatisticItemModel.name
            elementProductStatisticTvOrderCountValue.text = productStatisticItemModel.orderCount
            elementProductStatisticTvCountValue.text = productStatisticItemModel.count
            elementProductStatisticTvCostValue.text = productStatisticItemModel.cost
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementProductStatisticBinding {
        return ElementProductStatisticBinding.inflate(inflater, parent, false)
    }
}