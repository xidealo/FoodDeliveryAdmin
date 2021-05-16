package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.room.Ignore
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementTextBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticItem(var title: String, var orderList: List<Order>): AbstractBindingItem<ElementTextBinding>(),
    Parcelable {

    @IgnoredOnParcel
    @Ignore
    override val type = R.id.element_text_mcv_main

    override fun bindView(binding: ElementTextBinding, payloads: List<Any>) {
        binding.elementTextTvTitle.text = title
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementTextBinding {
        return ElementTextBinding.inflate(inflater, parent, false)
    }
}
