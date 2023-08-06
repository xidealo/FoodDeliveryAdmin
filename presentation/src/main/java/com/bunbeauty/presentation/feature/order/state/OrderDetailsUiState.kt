package com.bunbeauty.presentation.feature.order.state

import com.bunbeauty.domain.model.order.details.PaymentMethod

data class OrderDetailsUiState(
    val title: String,
    val state: State,
    val eventList: List<OrderDetailsEvent>,
) {

    sealed interface State {
        object Loading: State
        object Error: State
        data class Success(
            val dateTime: String,
            val deferredTime: HintWithValue?,
            val paymentMethod: PaymentMethod?,
            val receiptMethod: String,
            val address: String,
            val comment: String?,
            val status: String,
            val phoneNumber: String,
            val productList: List<Product>,
            val deliveryCost: String?,
            val finalCost: String,
        ): State
    }

    data class HintWithValue(
        val hint: String,
        val value: String,
    )

    data class Product(
        val title: String,
        val price: String,
        val count: String,
        val cost: String,
    )
}