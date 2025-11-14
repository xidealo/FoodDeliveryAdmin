package com.bunbeauty.domain.model.addition

data class UpdateAddition(
    val name: String? = null,
    val priority: Int? = null,
    val fullName: String? = null,
    val price: Int? = null,
    val photoLink: String? = null,
    val isVisible: Boolean? = null,
    val tag: String? = null,
    val newImageUri: String? = null,
) {
    companion object {
        val mock =
            UpdateAddition(
                name = null,
                priority = null,
                fullName = null,
                price = null,
                photoLink = null,
                isVisible = null,
                tag = null,
                newImageUri = null,
            )
    }
}
