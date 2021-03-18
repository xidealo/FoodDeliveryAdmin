package com.bunbeauty.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.presentation.navigator.ChangeStatusNavigator
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChangeStatusViewModel @Inject constructor(
    private val apiRepository: IApiRepository,
    private val dataStoreHelper: IDataStoreHelper
) : BaseViewModel<ChangeStatusNavigator>() {

    override var navigator: WeakReference<ChangeStatusNavigator>? = null

    fun cancelClick() {
        navigator?.get()?.closeDialog()
    }

    fun updateClick(){
        navigator?.get()?.updateClick()
    }

    fun changeStatus(order: Order, newStatus: OrderStatus) {
        viewModelScope.launch(IO) {
            val cafeId = dataStoreHelper.cafeId.first()
            apiRepository.updateOrder(cafeId, order.uuid, newStatus)
        }
    }
}