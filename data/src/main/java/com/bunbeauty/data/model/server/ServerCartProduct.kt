package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class ServerCartProduct(
    val count: Int = 0,
    var uuid: String = "",
    var name: String = "",
    var newPrice: Int = 0,
    var oldPrice: Int? = null,
    var utils: String = "",
    var nutrition: Int = 0,
    var description: String = "",
    var comboDescription: String? = null,
    var barcode: Int = 0,
)
