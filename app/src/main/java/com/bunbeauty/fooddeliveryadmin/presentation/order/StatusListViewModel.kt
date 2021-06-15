package com.bunbeauty.fooddeliveryadmin.presentation.order

import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.StatusItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.StatusListBottomSheetArgs
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import javax.inject.Inject

abstract class StatusListViewModel : BaseViewModel() {

    abstract val statusList: List<StatusItem>
}

class StatusListViewModelImpl @Inject constructor(
    private val args: StatusListBottomSheetArgs,
    private val stringUtil: IStringUtil
) : StatusListViewModel() {

    override val statusList: List<StatusItem>
        get() = when {
            args.isDeferred && args.isDelivery -> {
                arrayListOf(
                    StatusItem(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                    StatusItem(stringUtil.getOrderStatusString(ACCEPTED)),
                    StatusItem(stringUtil.getOrderStatusString(PREPARING)),
                    StatusItem(stringUtil.getOrderStatusString(SENT_OUT)),
                    StatusItem(stringUtil.getOrderStatusString(DELIVERED)),
                    StatusItem(stringUtil.getOrderStatusString(CANCELED)),
                )
            }
            args.isDeferred && !args.isDelivery -> {
                arrayListOf(
                    StatusItem(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                    StatusItem(stringUtil.getOrderStatusString(ACCEPTED)),
                    StatusItem(stringUtil.getOrderStatusString(PREPARING)),
                    StatusItem(stringUtil.getOrderStatusString(DONE)),
                    StatusItem(stringUtil.getOrderStatusString(DELIVERED)),
                    StatusItem(stringUtil.getOrderStatusString(CANCELED)),
                )
            }
            !args.isDeferred && args.isDelivery -> {
                arrayListOf(
                    StatusItem(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                    StatusItem(stringUtil.getOrderStatusString(PREPARING)),
                    StatusItem(stringUtil.getOrderStatusString(SENT_OUT)),
                    StatusItem(stringUtil.getOrderStatusString(DELIVERED)),
                    StatusItem(stringUtil.getOrderStatusString(CANCELED)),
                )
            }
            !args.isDeferred && !args.isDelivery -> {
                arrayListOf(
                    StatusItem(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                    StatusItem(stringUtil.getOrderStatusString(PREPARING)),
                    StatusItem(stringUtil.getOrderStatusString(DONE)),
                    StatusItem(stringUtil.getOrderStatusString(DELIVERED)),
                    StatusItem(stringUtil.getOrderStatusString(CANCELED)),
                )
            }
            else -> emptyList()
        }
}