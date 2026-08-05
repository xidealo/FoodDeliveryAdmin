package com.bunbeauty.domain.model.order.details

data class ClientUser(
    val uuid: String,
    val phoneNumber: String,
    val email: String?,
    val isProblematic: Boolean = false,
) {
    companion object {
        val mock =
            ClientUser(
                uuid = "",
                phoneNumber = "",
                email = null,
                isProblematic = false,
            )
    }
}
