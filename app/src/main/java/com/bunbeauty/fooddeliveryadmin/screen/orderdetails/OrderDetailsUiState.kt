package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import androidx.compose.ui.graphics.Color
import com.bunbeauty.presentation.feature.order.state.OrderDetailsEvent

data class OrderDetailsUiState(
    val title: String,
    val state: State,
    val eventList: List<OrderDetailsEvent>
) {

    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val dateTime: String,
            val deferredTime: HintWithValue?,
            val paymentMethod: String?,
            val receiptMethod: String,
            val address: String,
            val comment: String?,
            val status: String,
            val statusColor: Color,
            val phoneNumber: String,
            val productList: List<Product>,
            val percentDiscount: String?,
            val deliveryCost: String?,
            val finalCost: String
        ) : State
    }

    data class HintWithValue(
        val hint: String,
        val value: String
    )

    data class Product(
        val title: String,
        val price: String,
        val count: String,
        val cost: String,
        val description: String?
    )
}
