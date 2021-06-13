package com.bunbeauty.fooddeliveryadmin.utils

import com.bunbeauty.data.model.Address
import com.bunbeauty.domain.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import javax.inject.Inject

class StringUtil @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    IStringUtil {

    override fun toString(address: Address?): String {
        return if (address == null) {
            ""
        } else {
            address.street.name +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_house),
                        address.house
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_flat),
                        address.flat
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_entrance),
                        address.entrance
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_intercom),
                        address.intercom
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_floor),
                        address.floor
                    )
        }
    }

    fun getStringPart(constant: String, possiblyEmpty: String): String {
        return if (possiblyEmpty.isEmpty()) {
            ""
        } else {
            constant + possiblyEmpty
        }
    }
}