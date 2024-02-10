package com.bunbeauty.presentation.feature.order

import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.order.LoadOrderDetailsUseCase
import com.bunbeauty.domain.feature.order.UpdateOrderStatusUseCase
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import com.bunbeauty.presentation.feature.order.state.OrderDetailsDataState
import com.bunbeauty.presentation.feature.order.state.OrderDetailsEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderStatusMapper: OrderStatusMapper,
    private val loadOrderDetails: LoadOrderDetailsUseCase,
    private val updateOrderStatus: UpdateOrderStatusUseCase,
    private val resources: Resources,
) : BaseViewModel() {

    private val mutableDataState = MutableStateFlow(OrderDetailsDataState.crateInitialOrderDetailsDataState())
    val uiState: StateFlow<OrderDetailsDataState> = mutableDataState.asStateFlow()

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
        val orderDetails = mutableDataState.value.orderDetails ?: return
        if (orderDetails.status == OrderStatus.CANCELED) {
            mutableDataState.update { dataState ->
                dataState + OrderDetailsEvent.OpenWarningDialogEvent
            }
        } else {
            updateStatus(
                orderUuid = orderDetails.uuid,
                status = orderDetails.status
            )
        }
    }

    fun onCancellationConfirmed() {
        mutableDataState.value.orderDetails?.uuid?.let { orderUuid ->
            updateStatus(
                orderUuid = orderUuid,
                status = OrderStatus.CANCELED
            )
        }
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

    private fun updateStatus(orderUuid: String, status: OrderStatus) {
        viewModelScope.launchSafe(
            block = {
                updateOrderStatus(
                    orderUuid = orderUuid,
                    status = status,
                )
                mutableDataState.update { dataState ->
                    dataState + OrderDetailsEvent.GoBackEvent
                }
            },
            onError = {
                val errorMessage = resources.getString(R.string.error_order_details_can_not_save)
                mutableDataState.update { dataState ->
                    dataState + OrderDetailsEvent.ShowErrorMessage(errorMessage)
                }
            }
        )
    }

}
