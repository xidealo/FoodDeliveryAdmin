package com.bunbeauty.domain.model.addition

data class Addition(
    val uuid: String,
    val name: String,
    val priority: Int,
    val fullName: String?,
    val price: Int?,
    val photoLink: String,
    val isVisible: Boolean,
    val tag: String?
) {
    companion object {
        val mock = Addition(
            uuid = "",
            name = "",
            priority = 0,
            fullName = null,
            price = null,
            photoLink = "",
            isVisible = false,
            tag = null
        )
    }
}
