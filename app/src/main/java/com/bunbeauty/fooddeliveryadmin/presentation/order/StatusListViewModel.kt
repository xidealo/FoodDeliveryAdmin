package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.fooddeliveryadmin.extensions.navArgs
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.StatusItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.StatusListBottomSheetArgs
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatusListViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val args: StatusListBottomSheetArgs by savedStateHandle.navArgs()

    val statusList: List<StatusItem>
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