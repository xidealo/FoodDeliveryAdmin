package com.bunbeauty.fooddeliveryadmin.ui.items

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementTextBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeriodItem(val period: String): AbstractBindingItem<ElementTextBinding>(), Parcelable {

    @IgnoredOnParcel
    override val type = R.id.element_text_mvc_main

    override fun bindView(binding: ElementTextBinding, payloads: List<Any>) {
        binding.elementTextTvTitle.text = period
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementTextBinding {
        return ElementTextBinding.inflate(inflater, parent, false)
    }
}