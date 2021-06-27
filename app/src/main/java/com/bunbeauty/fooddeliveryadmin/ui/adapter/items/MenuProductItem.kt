package com.bunbeauty.fooddeliveryadmin.ui.adapter.items

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.setImage
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuProductItem(
    val name: String,
    val photoLink: String,
    val visible: Boolean,
    val newCost: String,
    val oldCost: String,
    val menuProduct: MenuProduct
) : AbstractBindingItem<ElementMenuProductBinding>(), Parcelable {

    @IgnoredOnParcel
    override val type = R.id.element_cart_product_mcv_main

    override fun bindView(binding: ElementMenuProductBinding, payloads: List<Any>) {
        binding.elementMenuProductIvPhoto.setImage(photoLink)
        binding.elementMenuProductTvTitle.text = name
        binding.elementMenuProductIvVisible.isVisible = visible
        binding.elementMenuProductTvOldCost.strikeOutText()
        binding.elementMenuProductTvOldCost.text = oldCost
        binding.elementMenuProductTvNewCost.text = newCost
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementMenuProductBinding {
        return ElementMenuProductBinding.inflate(inflater, parent, false)
    }
}