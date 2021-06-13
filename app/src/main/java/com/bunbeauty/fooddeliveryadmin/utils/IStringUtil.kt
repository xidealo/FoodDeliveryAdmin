package com.bunbeauty.fooddeliveryadmin.utils

import com.bunbeauty.data.model.Address

interface IStringUtil {
    fun toString(address: Address?): String
}