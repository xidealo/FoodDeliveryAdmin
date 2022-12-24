package com.bunbeauty.fooddeliveryadmin.screen.order_details

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.ORDER_UUID_ARGS_KEY
import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.CANCELED
import com.bunbeauty.domain.model.order.OrderDetails
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.fooddeliveryadmin.core_ui.ListItem
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.order_details.item.OrderDetailsItem
import com.bunbeauty.fooddeliveryadmin.screen.order_details.item.OrderProductItem
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.navArg
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productUtil: IProductUtil,
    private val stringUtil: IStringUtil,
    private val resources: Resources,
    private val dateTimeUtil: IDateTimeUtil,
    private val dataStoreRepo: DataStoreRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val orderUuid: String by savedStateHandle.navArg(ORDER_UUID_ARGS_KEY)

    private val mutableOrderDetailsState: MutableStateFlow<OrderDetailsState> =
        MutableStateFlow(OrderDetailsState())
    val orderDetailsState: StateFlow<OrderDetailsState> = mutableOrderDetailsState.asStateFlow()

    init {
        loadOrder(orderUuid)
    }

    fun onStatusClicked() {
        val availableStatusList = mutableOrderDetailsState.value.orderDetails?.availableStatusList
        if (!availableStatusList.isNullOrEmpty()) {
            val statusList = availableStatusList.map { orderStatus ->
                Option(
                    id = orderStatus.name,
                    title = stringUtil.getOrderStatusString(orderStatus),
                )
            }
            val openStatusListEvent =
                OrderDetailsState.Event.OpenStatusListEvent(statusList = statusList)
            mutableOrderDetailsState.update { orderDetailsState ->
                orderDetailsState + openStatusListEvent
            }
        }
    }

    fun onStatusSelected(statusName: String) {
        val orderStatus = OrderStatus.valueOf(statusName)
        mutableOrderDetailsState.update { orderDetailsState ->
            val orderDetails = orderDetailsState.orderDetails
            if (orderDetails == null) {
                orderDetailsState
            } else {
                val updatedOrderDetails = orderDetails.copy(status = orderStatus)
                orderDetailsState.copy(
                    orderDetails = updatedOrderDetails,
                    selectedStatus = orderStatus,
                    itemModelList = buildItemModelList(updatedOrderDetails)
                )
            }
        }
    }

    fun onSaveClicked() {
        val selectedStatus = mutableOrderDetailsState.value.selectedStatus ?: return
        if (selectedStatus == CANCELED) {
            mutableOrderDetailsState.update { orderDetailsState ->
                orderDetailsState + OrderDetailsState.Event.OpenWarningDialogEvent
            }
        } else {
            updateStatus(selectedStatus)
        }
    }

    fun onCancellationConfirmed() {
        updateStatus(CANCELED)
    }

    fun onRetryClicked(retryAction: OrderDetailsState.RetryAction) {
        when (retryAction) {
            OrderDetailsState.RetryAction.LOAD_ORDER -> {
                loadOrder(orderUuid)
            }
            OrderDetailsState.RetryAction.SAVE_STATUS -> {
                val selectedStatus = mutableOrderDetailsState.value.selectedStatus ?: return
                updateStatus(selectedStatus)
            }
        }
    }

    fun consumeEvents(events: List<OrderDetailsState.Event>) {
        mutableOrderDetailsState.update { orderDetailsState ->
            orderDetailsState - events
        }
    }

    private fun loadOrder(orderUuid: String) {
        mutableOrderDetailsState.update { orderDetailsState ->
            orderDetailsState.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                dataStoreRepo.token.firstOrNull()?.let { token ->
                    orderRepository.loadOrderByUuid(
                        token = token,
                        orderUuid = orderUuid
                    )?.let { order ->
                        updateOrderDetailsState(order)
                    }
                }
            } catch (exception: Exception) {
                val event = OrderDetailsState.Event.OpenErrorDialogEvent(
                    OrderDetailsState.RetryAction.LOAD_ORDER
                )
                mutableOrderDetailsState.update { orderDetailsState ->
                    orderDetailsState.copy(isLoading = false) + event
                }
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun updateOrderDetailsState(orderDetails: OrderDetails) {
        mutableOrderDetailsState.update { orderDetailsState ->
            orderDetailsState.copy(
                orderDetails = orderDetails,
                itemModelList = buildItemModelList(orderDetails),
                deliveryCost = orderDetails.deliveryCost?.let { deliveryCost ->
                    if (deliveryCost == 0) {
                        resources.getString(R.string.msg_order_delivery_free)
                    } else {
                        resources.getString(R.string.with_ruble, deliveryCost)
                    }
                },
                oldFinalCost = orderDetails.oldTotalCost?.let { oldTotalCost ->
                    resources.getString(R.string.with_ruble, oldTotalCost)
                },
                newFinalCost = resources.getString(R.string.with_ruble, orderDetails.newTotalCost),
                selectedStatus = orderDetails.status,
                isLoading = false
            )
        }
    }

    private fun buildItemModelList(order: OrderDetails): List<ListItem> = buildList {
        add(
            OrderDetailsItem(
                uuid = order.uuid,
                phone = order.clientUser.phoneNumber,
                time = dateTimeUtil.getDateTimeDDMMHHMM(order.time),
                receiveMethod = stringUtil.getReceiveMethodString(order.isDelivery),
                deferredTime = order.deferredTime?.let { dateTimeUtil.getTimeHHMM(it) },
                address = stringUtil.getOrderAddressString(order.address),
                comment = order.comment,
                status = stringUtil.getOrderStatusString(order.status)
            )
        )
        order.oderProductList.map { cartProduct ->
            val oldCost = productUtil.getCartProductOldCost(cartProduct)
            val newCost = productUtil.getCartProductNewCost(cartProduct)

            OrderProductItem(
                uuid = cartProduct.uuid,
                name = productUtil.getPositionName(cartProduct),
                count = stringUtil.getProductCountString(cartProduct.count),
                oldCost = stringUtil.getCostString(oldCost),
                newCost = stringUtil.getCostString(newCost)
            )
        }.let { orderProductList ->
            addAll(orderProductList)
        }
    }

    private fun updateStatus(selectedStatus: OrderStatus) {
        viewModelScope.launch {
            val token = dataStoreRepo.token.first()
            try {
                orderRepository.updateStatus(
                    token = token,
                    orderUuid = orderUuid,
                    status = selectedStatus
                )
                mutableOrderDetailsState.update { orderDetailsState ->
                    orderDetailsState + OrderDetailsState.Event.GoBackEvent
                }
            } catch (exception: Exception) {
                mutableOrderDetailsState.update { orderDetailsState ->
                    orderDetailsState + OrderDetailsState.Event.OpenErrorDialogEvent(OrderDetailsState.RetryAction.SAVE_STATUS)
                }
            }
        }
    }
}