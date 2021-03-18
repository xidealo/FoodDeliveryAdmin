package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.room.Ignore
import com.bunbeauty.data.model.Address
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementAddressBinding
import com.bunbeauty.fooddeliveryadmin.utils.string.IStringHelper
import com.mikepenz.fastadapter.binding.AbstractBindingItem

data class AddressItem(val address: Address, val stringHelper: IStringHelper) :
    AbstractBindingItem<ElementAddressBinding>() {

    @Ignore
    override val type = R.id.element_address_mcv_main

    override fun bindView(binding: ElementAddressBinding, payloads: List<Any>) {
        binding.stringHelper = stringHelper
        binding.address = address
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementAddressBinding {
        return ElementAddressBinding.inflate(inflater, parent, false)
    }
}
