package com.bunbeauty.presentation.feature.order.state

import com.bunbeauty.domain.model.order.details.PaymentMethod

data class OrderDetailsUiState(
    val title: String,
    val state: State,
    val eventList: List<OrderDetailsEvent>,
) {

    sealed interface State {
        data object Loading : State
        data object Error : State
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
            val percentDiscount: String?,
            val deliveryCost: String?,
            val finalCost: String,
            val oldFinalCost: String?,
        ) : State
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