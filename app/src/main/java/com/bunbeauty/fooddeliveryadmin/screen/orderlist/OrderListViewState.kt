package com.bunbeauty.fooddeliveryadmin.screen.orderlist

import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class OrderListViewState(
    val state: State
) : BaseViewState {

    @Immutable
    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val cafeAddress: String,
            val orderList: ImmutableList<OrderItem>,
            val connectionError: Boolean,
            val refreshing: Boolean,
            val loadingOrderList: Boolean
        ) : State
    }

    @Immutable
    data class OrderItem(
        val uuid: String,
        val status: OrderStatus,
        val statusString: String,
        val code: String,
        val deferredTime: String,
        val dateTime: String
    )
}
