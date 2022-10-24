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
import com.bunbeauty.domain.util.order.IOrderUtil
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productUtil: IProductUtil,
    private val orderUtil: IOrderUtil,
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

    private fun loadOrder(orderUuid: String) {
        mutableOrderDetailsState.update { orderDetailsState ->
            orderDetailsState.copy(isLoading = true)
        }
        viewModelScope.launch {
            dataStoreRepo.token.firstOrNull()?.let { token ->
                orderRepository.loadOrderByUuid(
                    token = token, orderUuid = orderUuid
                )?.let { order ->
                    updateOrderDetailsState(order)
                }
            }
        }
    }

    fun onStatusClicked() {
        val availableStatusList = mutableOrderDetailsState.value.order?.availableStatusList
        if (!availableStatusList.isNullOrEmpty()) {
            val statusList = availableStatusList.map { orderStatus ->
                Option(
                    id = orderStatus.name,
                    title = stringUtil.getOrderStatusString(orderStatus),
                )
            }
            val openStatusListEvent = OrderDetailsState.OpenStatusListEvent(statusList = statusList)
            mutableOrderDetailsState.update { orderDetailsState ->
                orderDetailsState.copy(eventList = orderDetailsState.eventList + openStatusListEvent)
            }
        }
    }

    fun onStatusSelected(statusName: String) {
        val orderStatus = OrderStatus.valueOf(statusName)
        mutableOrderDetailsState.update { orderDetailsState ->
            val order = orderDetailsState.order
            if (order == null) {
                orderDetailsState
            } else {
                val updatedOrder = order.copy(orderStatus = orderStatus)
                orderDetailsState.copy(
                    order = updatedOrder,
                    selectedStatus = orderStatus,
                    itemModelList = buildItemModelList(updatedOrder)
                )
            }
        }
    }

    fun onSaveClicked() {
        val selectedStatus = mutableOrderDetailsState.value.selectedStatus ?: return
        if (selectedStatus == CANCELED) {
            mutableOrderDetailsState.update { orderDetailsState ->
                orderDetailsState.copy(eventList = orderDetailsState.eventList + OrderDetailsState.OpenWarningDialogEvent)
            }
        } else {
            updateStatus(selectedStatus)
        }
    }

    fun onCancellationConfirmed() {
        updateStatus(CANCELED)
    }

    fun consumeEvents(events: List<OrderDetailsState.Event>) {
        mutableOrderDetailsState.update { orderDetailsState ->
            orderDetailsState.copy(eventList = orderDetailsState.eventList - events.toSet())
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun updateOrderDetailsState(order: OrderDetails) {
        val oldOrderCost = orderUtil.getOldOrderCost(order)
        val newOrderCost = orderUtil.getNewOrderCost(order)
        mutableOrderDetailsState.update { orderDetailsState ->
            orderDetailsState.copy(
                order = order,
                itemModelList = buildItemModelList(order),
                deliveryCost = order.deliveryCost?.let { deliveryCost ->
                    if (deliveryCost == 0) {
                        resources.getString(R.string.msg_order_delivery_free)
                    } else {
                        resources.getString(R.string.with_ruble, deliveryCost)
                    }
                },
                discount = order.discount?.let { discount ->
                    resources.getString(R.string.with_ruble_negative, discount)
                },
                oldFinalCost = oldOrderCost?.let { resources.getString(R.string.with_ruble, it) },
                newFinalCost = resources.getString(R.string.with_ruble, newOrderCost),
                selectedStatus = order.orderStatus,
                isLoading = false
            )
        }
    }

    private fun buildItemModelList(order: OrderDetails): List<ListItem> = buildList {
        add(
            OrderDetailsItem(
                uuid = order.uuid,
                phone = order.phone,
                time = dateTimeUtil.getTimeHHMM(order.time),
                receiveMethod = stringUtil.getReceivingMethodString(order.delivery),
                deferredTime = order.deferred?.let { dateTimeUtil.getTimeHHMM(it) },
                address = order.address,
                comment = order.comment,
                status = stringUtil.getOrderStatusString(order.orderStatus)
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
            orderRepository.updateStatus(
                token = token,
                orderUuid = orderUuid,
                status = selectedStatus
            )
            mutableOrderDetailsState.update { orderDetailsState ->
                orderDetailsState.copy(eventList = orderDetailsState.eventList + OrderDetailsState.GoBackEvent)
            }
        }
    }
}