package com.bunbeauty.fooddeliveryadmin.ui.adapter.items

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.room.Ignore
import com.bunbeauty.data.model.cart_product.CartProductUI
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementCartProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProductItem(
    val cartProductUI: CartProductUI
) : AbstractBindingItem<ElementCartProductBinding>(), Parcelable {

    @IgnoredOnParcel
    @Ignore
    override val type = R.id.element_cart_product_mcv_main

    override fun bindView(binding: ElementCartProductBinding, payloads: List<Any>) {
        Picasso.get()
            .load(cartProductUI.photoLink)
            .fit()
            .placeholder(R.drawable.default_product)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(binding.elementCartProductIvPhoto)
        binding.elementCartProductTvTitle.text = cartProductUI.name
        binding.elementCartProductTvCount.text = cartProductUI.count
        binding.elementCartProductTvOldCost.strikeOutText()
        binding.elementCartProductTvOldCost.text = cartProductUI.oldCost
        binding.elementCartProductTvNewCost.text = cartProductUI.newCost
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementCartProductBinding {
        return ElementCartProductBinding.inflate(inflater, parent, false)
    }
}