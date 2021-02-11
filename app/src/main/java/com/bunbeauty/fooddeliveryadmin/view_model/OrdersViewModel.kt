package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.local.db.cafe.CafeRepo
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersNavigator
import com.bunbeauty.fooddeliveryadmin.utils.string.IStringHelper
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
        cafeRepo: CafeRepo,
        private val apiRepository: IApiRepository,
        private val stringHelper: IStringHelper,
        private val dataStoreHelper: IDataStoreHelper
) : BaseViewModel<OrdersNavigator>() {

    override var navigator: WeakReference<OrdersNavigator>? = null
    val addOrderWithCartProducts = ObservableField<Order?>()
    val addOrderWithCartProductList = ObservableField<List<Order>>()
    val replaceOrderWithCartProducts = ObservableField<Order?>()

    private val cafeListLiveData = cafeRepo.cafeListLiveData
    val cafeAddressListLiveData = map(cafeListLiveData) { cafeList ->
        cafeList.map { cafe ->
            stringHelper.toString(cafe.address)
        }
    }

    val cafeAddressLiveData: LiveData<String> = switchMap(dataStoreHelper.cafeId.asLiveData()) { cafeId ->
        map(cafeListLiveData) { cafeList ->
            val cafe = cafeList.find { cafe ->
                cafe.cafeEntity.id == cafeId
            }
            if (cafe == null) {
                ""
            } else {
                stringHelper.toString(cafe.address)
            }
        }
    }
    val addedOrderListLiveData = switchMap(dataStoreHelper.cafeId.asLiveData()) { cafeId ->
        apiRepository.getAddedOrderListLiveData(cafeId)
    }

    val updatedOrderListLiveData = apiRepository.updatedOrderListLiveData

    fun setCafe(address: String) {
        val cafeId = cafeListLiveData.value?.find { cafe ->
            stringHelper.toString(cafe.address) == address
        }?.cafeEntity?.id
        cafeId?.let {
            viewModelScope.launch(IO) {
                dataStoreHelper.saveCafeId(it)
            }
        }
    }

}