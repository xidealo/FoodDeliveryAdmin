package com.bunbeauty.presentation.feature.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.order.usecase.LoadOrderDetailsUseCase
import com.bunbeauty.domain.feature.order.usecase.UpdateOrderStatusUseCase
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.order.state.OrderDetailsState
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class OrderDetailsViewModel(
    private val loadOrderDetails: LoadOrderDetailsUseCase,
    private val updateOrderStatus: UpdateOrderStatusUseCase
) : BaseStateViewModel<OrderDetailsState.DataState, OrderDetailsState.Action, OrderDetailsState.Event>(
    initState = OrderDetailsState.DataState(
        state = OrderDetailsState.DataState.State.LOADING,
        code = "",
        orderDetails = null,
        saving = false,
        showStatusList = false
    )
) {

    override fun reduce(action: OrderDetailsState.Action, dataState: OrderDetailsState.DataState) {
        when (action) {
            is OrderDetailsState.Action.Init -> {
                setupOrder(
                    orderUuid = action.orderUuid,
                    orderCode = action.orderCode
                )
            }

            OrderDetailsState.Action.OnBackClicked -> onBackClicked()

            OrderDetailsState.Action.OnSaveClicked -> onSaveClicked()

            OrderDetailsState.Action.OnStatusClicked -> onStatusClicked()

            is OrderDetailsState.Action.OnSelectStatusClicked -> {
                onStatusSelected(orderStatus = action.status)
            }

            OrderDetailsState.Action.OnCloseStatusClicked -> closeStatusBottomSheet()
        }
    }

    private fun setupOrder(orderUuid: String, orderCode: String) {
        setState {
            copy(
                state = OrderDetailsState.DataState.State.LOADING,
                code = orderCode
            )
        }
        viewModelScope.launchSafe(
            block = {
                val orderDetails = loadOrderDetails(orderUuid)
                updateOrderDetailsState(orderDetails)
            },
            onError = {
                setState {
                    copy(state = OrderDetailsState.DataState.State.ERROR)
                }
            }
        )
    }

    private fun onStatusClicked() {
        val availableStatusList = mutableDataState.value.orderDetails?.availableStatusList
        if (!availableStatusList.isNullOrEmpty()) {
            setState {
                copy(
                    showStatusList = true
                )
            }
        }
    }

    private fun closeStatusBottomSheet() {
        setState {
            copy(
                showStatusList = false
            )
        }
    }

    private fun onStatusSelected(orderStatus: OrderStatus) {
        setState {
            copy(
                orderDetails = orderDetails?.copy(
                    status = orderStatus
                ),
                showStatusList = false
            )
        }
    }

    private fun onBackClicked() {
        sendEvent {
            OrderDetailsState.Event.GoBackEvent
        }
    }

    private fun onSaveClicked() {
        val orderDetails = mutableDataState.value.orderDetails ?: return
        if (orderDetails.status == OrderStatus.CANCELED) {
            sendEvent {
                OrderDetailsState.Event.OpenWarningDialogEvent
            }
        } else {
            updateStatus(
                orderUuid = orderDetails.uuid,
                status = orderDetails.status,
                orderCode = orderDetails.code
            )
        }
    }

    fun onCancellationConfirmed() {
        val orderDetails = mutableDataState.value.orderDetails ?: return

        updateStatus(
            orderUuid = orderDetails.uuid,
            status = OrderStatus.CANCELED,
            orderCode = orderDetails.code
        )
    }

    private fun updateOrderDetailsState(orderDetails: OrderDetails?) {
        if (orderDetails == null) {
            setState {
                copy(state = OrderDetailsState.DataState.State.ERROR)
            }

            return
        }

        setState {
            copy(
                state = OrderDetailsState.DataState.State.SUCCESS,
                orderDetails = orderDetails
            )
        }
    }

    private fun updateStatus(orderUuid: String, status: OrderStatus, orderCode: String) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(saving = true)
                }
                updateOrderStatus(
                    orderUuid = orderUuid,
                    status = status
                )
                sendEvent {
                    OrderDetailsState.Event.SavedEvent(orderCode = orderCode)
                }
            },
            onError = {
                sendEvent {
                    OrderDetailsState.Event.ShowErrorMessage(messageId = R.string.error_order_details_can_not_save)
                }
            }
        )
    }
}
