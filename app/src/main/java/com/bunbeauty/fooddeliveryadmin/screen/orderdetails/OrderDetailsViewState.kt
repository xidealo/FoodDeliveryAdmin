package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class OrderDetailsViewState(
    val title: String,
    val state: State,
) : BaseViewState {
    @Immutable
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
            val productList: ImmutableList<Product>,
            val percentDiscount: String?,
            val deliveryCost: String?,
            val finalCost: String,
            val saving: Boolean,
            val statusListUI: StatusListUI,
        ) : State {
            @Immutable
            data class StatusListUI(
                val isShown: Boolean,
                val statusList: ImmutableList<StatusItem>,
            ) {
                @Immutable
                data class StatusItem(
                    val orderStatus: OrderStatus,
                    val status: String,
                )
            }
        }
    }

    @Immutable
    data class HintWithValue(
        val hint: String,
        val value: String,
    )

    @Immutable
    data class Product(
        val title: String,
        val price: String,
        val count: String,
        val cost: String,
        val description: String?,
    )
}
