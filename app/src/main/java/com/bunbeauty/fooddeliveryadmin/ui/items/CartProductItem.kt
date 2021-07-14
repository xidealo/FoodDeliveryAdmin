package com.bunbeauty.fooddeliveryadmin.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementCartProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.presentation.model.CartProductItemModel
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel

data class CartProductItem(
    val cartProductItemModel: CartProductItemModel
) : AbstractBindingItem<ElementCartProductBinding>() {

    override val type = R.id.element_cart_product_mcv_main

    override fun bindView(binding: ElementCartProductBinding, payloads: List<Any>) {
        with(binding) {
            elementCartProductTvTitle.text = cartProductItemModel.name
            elementCartProductTvCount.text = cartProductItemModel.count
            elementCartProductTvOldCost.strikeOutText()
            elementCartProductTvOldCost.text = cartProductItemModel.oldCost
            elementCartProductTvNewCost.text = cartProductItemModel.newCost
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementCartProductBinding {
        return ElementCartProductBinding.inflate(inflater, parent, false)
    }
}