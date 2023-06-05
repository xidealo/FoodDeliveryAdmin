package com.bunbeauty.presentation.feature.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.order.LoadOrderDetailsUseCase
import com.bunbeauty.domain.model.order.OrderDetails
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.feature.order.mapper.OrderDetailsStateMapper
import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import com.bunbeauty.presentation.feature.order.state.OrderDetailsDataState
import com.bunbeauty.presentation.feature.order.state.OrderDetailsEvent
import com.bunbeauty.presentation.feature.order.state.OrderDetailsUiState
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderDetailsStateMapper: OrderDetailsStateMapper,
    private val orderStatusMapper: OrderStatusMapper,
    private val loadOrderDetails: LoadOrderDetailsUseCase,
) : BaseViewModel() {

    private val mutableDataState =
        MutableStateFlow(OrderDetailsDataState.crateInitialOrderDetailsDataState())
    val uiState: StateFlow<OrderDetailsUiState> =
        mutableDataState.mapToStateFlow(viewModelScope, orderDetailsStateMapper::map)

    fun setupOrder(orderUuid: String, orderCode: String) {
        mutableDataState.update { dataState ->
            dataState.copy(
                state = OrderDetailsDataState.State.LOADING,
                code = orderCode,
            )
        }
        viewModelScope.launchSafe(
            block = {
                val orderDetails = loadOrderDetails(orderUuid)
                updateOrderDetailsState(orderDetails)
            },
            onError = {
                mutableDataState.update { dataState ->
                    dataState.copy(state = OrderDetailsDataState.State.ERROR)
                }
            }
        )
    }

    fun onStatusClicked() {
        val availableStatusList = mutableDataState.value.orderDetails?.availableStatusList
        if (!availableStatusList.isNullOrEmpty()) {
            val statusList = availableStatusList.map { orderStatus ->
                Option(
                    id = orderStatus.name,
                    title = orderStatusMapper.map(orderStatus),
                )
            }
            mutableDataState.update { dataState ->
                dataState + OrderDetailsEvent.OpenStatusListEvent(statusList)
            }
        }
    }

    fun onStatusSelected(statusName: String) {
        mutableDataState.update { dataState ->
            dataState.copy(
                orderDetails = dataState.orderDetails?.copy(
                    status = OrderStatus.valueOf(statusName)
                )
            )
        }
    }

    fun onSaveClicked() {
//        val selectedStatus = mutableOrderDetailsState.value.selectedStatus ?: return
//        if (selectedStatus == CANCELED) {
//            mutableOrderDetailsState.update { orderDetailsState ->
//                orderDetailsState + OrderDetailsState.Event.OpenWarningDialogEvent
//            }
//        } else {
//            updateStatus(selectedStatus)
//        }
    }

    fun onCancellationConfirmed() {
        //updateStatus(CANCELED)
    }

    fun consumeEvents(events: List<OrderDetailsEvent>) {
        mutableDataState.update { dataState ->
            dataState - events
        }
    }

    private fun updateOrderDetailsState(orderDetails: OrderDetails?) {
        if (orderDetails == null) {
            mutableDataState.update { dataState ->
                dataState.copy(state = OrderDetailsDataState.State.ERROR)
            }

            return
        }

        mutableDataState.update { dataState ->
            dataState.copy(
                state = OrderDetailsDataState.State.SUCCESS,
                orderDetails = orderDetails,
            )
        }
    }

}
