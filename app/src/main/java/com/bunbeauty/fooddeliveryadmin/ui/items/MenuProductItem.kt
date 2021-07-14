package com.bunbeauty.fooddeliveryadmin.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.presentation.model.MenuProductItemModel

data class MenuProductItem(
    val menuProductItemModel: MenuProductItemModel
) : ImageItem<ElementMenuProductBinding>() {

    override val type = R.id.element_cart_product_mcv_main

    override fun bindView(binding: ElementMenuProductBinding, payloads: List<Any>) {
        with(binding) {
            setImage(elementMenuProductIvPhoto, menuProductItemModel.photoLink)
            elementMenuProductTvTitle.text = menuProductItemModel.name
            elementMenuProductIvVisible.isVisible = menuProductItemModel.visible
            elementMenuProductTvOldCost.strikeOutText()
            elementMenuProductTvOldCost.text = menuProductItemModel.oldCost
            elementMenuProductTvNewCost.text = menuProductItemModel.newCost
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementMenuProductBinding {
        return ElementMenuProductBinding.inflate(inflater, parent, false)
    }
}