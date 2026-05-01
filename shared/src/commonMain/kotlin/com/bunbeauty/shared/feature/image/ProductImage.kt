package com.bunbeauty.shared.feature.image

import com.bunbeauty.shared.feature.menulist.common.FieldData

data class ProductImage(
    val photoLink: String,
    val newImageUri: String?,
)

data class EditImageFieldData(
    override val value: ProductImage?,
    override val isError: Boolean,
) : FieldData<ProductImage?>()

data class ImageFieldData(
    override val value: String?,
    override val isError: Boolean,
) : FieldData<String?>()
