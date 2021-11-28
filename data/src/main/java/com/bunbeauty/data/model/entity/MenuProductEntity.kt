package com.bunbeauty.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.domain.enums.ProductCode

@Entity
data class MenuProductEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val cost: Int,
    val discountCost: Int?,
    val weight: Int?,
    val description: String,
    val comboDescription: String?,
    val photoLink: String,
    val onFire: Boolean,
    val inOven: Boolean,
    val productCode: ProductCode,
    val barcode: Int?,
    val visible: Boolean,
)