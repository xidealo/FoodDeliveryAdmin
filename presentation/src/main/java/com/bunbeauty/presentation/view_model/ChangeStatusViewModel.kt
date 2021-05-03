package com.bunbeauty.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.repository.order.OrderRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeStatusViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val dataStoreHelper: IDataStoreHelper
) : BaseViewModel() {
    fun changeStatus(order: Order) {
        viewModelScope.launch(IO) {
            order.cafeId = dataStoreHelper.cafeId.first()
            orderRepo.update(order)
        }
    }
}