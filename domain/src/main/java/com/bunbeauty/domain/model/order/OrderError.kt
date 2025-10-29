package com.bunbeauty.domain.model.order

data class OrderError(
    val message: String
) {
    companion object {
        val mock = OrderError(
            message = ""
        )
    }
}
