package com.bunbeauty.fooddeliveryadmin.ui.items

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementCartProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProductItem(
    val name: String,
    val photoLink: String,
    val count: String,
    val oldCost: String,
    val newCost: String,
) : AbstractBindingItem<ElementCartProductBinding>(), Parcelable {

    @IgnoredOnParcel
    override val type = R.id.element_cart_product_mcv_main

    override fun bindView(binding: ElementCartProductBinding, payloads: List<Any>) {
        binding.elementCartProductTvTitle.text = name
        binding.elementCartProductTvCount.text = count
        binding.elementCartProductTvOldCost.strikeOutText()
        binding.elementCartProductTvOldCost.text = oldCost
        binding.elementCartProductTvNewCost.text = newCost
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementCartProductBinding {
        return ElementCartProductBinding.inflate(inflater, parent, false)
    }
}