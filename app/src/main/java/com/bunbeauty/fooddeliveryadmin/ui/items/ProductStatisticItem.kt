package com.bunbeauty.fooddeliveryadmin.ui.items

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementProductStatisticBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductStatisticItem(
    val name: String,
    val photoLink: String,
    val orderCount: String,
    val count: String,
    val cost: String
) : AbstractBindingItem<ElementProductStatisticBinding>(), Parcelable {

    @IgnoredOnParcel
    override val type = R.id.element_product_statistic_mcv_main

    override fun bindView(binding: ElementProductStatisticBinding, payloads: List<Any>) {
        binding.elementProductStatisticTvName.text = name
        binding.elementProductStatisticTvOrderCountValue.text = orderCount
        binding.elementProductStatisticTvCountValue.text = count
        binding.elementProductStatisticTvCostValue.text = cost
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementProductStatisticBinding {
        return ElementProductStatisticBinding.inflate(inflater, parent, false)
    }
}