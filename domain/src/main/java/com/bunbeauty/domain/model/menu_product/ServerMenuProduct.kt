package com.bunbeauty.domain.model.menu_product

data class ServerMenuProduct(
    var uuid: String? = null,
    var name: String = "",
    var visible: Boolean = true,
    var productCode: String = "",
    var cost: Int = 0,
    var discountCost: Int? = null,
    var weight: Int? = null,
    var description: String = "",
    var comboDescription: String? = null,
    var photoLink: String = "",
    var onFire: Boolean = false,
    var inOven: Boolean = false,
    var barcode: Int? = 0
)