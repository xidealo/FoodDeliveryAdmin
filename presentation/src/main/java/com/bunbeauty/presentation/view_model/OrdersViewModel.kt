package com.bunbeauty.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.asLiveData
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.presentation.navigator.OrdersNavigator
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