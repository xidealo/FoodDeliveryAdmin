package com.bunbeauty.domain.model.order

sealed interface OrderListResult {

    class Success(val orderList: List<Order>): OrderListResult
    object Error: OrderListResult

    fun map(block: (List<Order>) -> List<Order>): OrderListResult {
        return if (this is Success) {
            Success(block(orderList))
        } else {
            this
        }
    }


}