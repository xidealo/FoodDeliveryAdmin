package com.bunbeauty.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.asLiveData
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.string_helper.IStringHelper
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
    private val apiRepository: IApiRepository,
    private val stringHelper: IStringHelper,
    cafeRepo: CafeRepo,
    dataStoreHelper: IDataStoreHelper
) : BaseViewModel() {

    private val cafeListLiveData = cafeRepo.cafeListLiveData
    val cafeAddressLiveData: LiveData<String> =
        switchMap(dataStoreHelper.cafeId.asLiveData()) { cafeId ->
            map(cafeListLiveData) { cafeList ->
                val address = cafeList.find { cafe ->
                    cafe.cafeEntity.id == cafeId
                }?.address
                if (address == null) {
                    ""
                } else {
                    stringHelper.toString(address)
                }
            }
        }

    val addedOrderListLiveData = switchMap(dataStoreHelper.cafeId.asLiveData()) { cafeId ->
        apiRepository.getAddedOrderListLiveData(cafeId)
    }
    val updatedOrderListLiveData = apiRepository.updatedOrderListLiveData

    fun removeOrder(order: Order){
        apiRepository.delete(order)
    }
}