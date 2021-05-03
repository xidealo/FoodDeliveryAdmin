package com.bunbeauty.presentation.view_model

import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.Address
import com.bunbeauty.domain.repository.cafe.CafeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddressListViewModel @Inject constructor(
    private val dataStoreHelper: IDataStoreHelper,
    cafeRepo: CafeRepo
) : BaseViewModel() {

    private val cafeListLiveData = cafeRepo.cafeListFlow.asLiveData()
    val cafeAddressListLiveData = Transformations.map(cafeListLiveData) { cafeList ->
        cafeList.mapNotNull { cafe ->
            cafe.address
        }
    }

    fun setCafe(address: Address) {
        val cafeId = cafeListLiveData.value?.find { cafe ->
            cafe.address?.id == address.id
        }?.cafeEntity?.id
        cafeId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                dataStoreHelper.saveCafeId(it)
            }
        }
    }
}