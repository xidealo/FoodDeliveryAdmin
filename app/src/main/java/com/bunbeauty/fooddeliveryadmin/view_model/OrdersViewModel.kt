package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.asLiveData
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.local.db.cafe.CafeRepo
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersNavigator
import com.bunbeauty.fooddeliveryadmin.utils.string.IStringHelper
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
        private val apiRepository: IApiRepository,
        private val stringHelper: IStringHelper,
        cafeRepo: CafeRepo,
        dataStoreHelper: IDataStoreHelper
) : BaseViewModel<OrdersNavigator>() {

    override var navigator: WeakReference<OrdersNavigator>? = null

    private val cafeListLiveData = cafeRepo.cafeListLiveData
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


}