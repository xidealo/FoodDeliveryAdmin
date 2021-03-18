package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.data.local.db.cafe.CafeRepo
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.data.model.Address
import com.bunbeauty.fooddeliveryadmin.ui.address_list.AddressListNavigator
import com.bunbeauty.fooddeliveryadmin.utils.string.IStringHelper
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

class AddressListViewModel @Inject constructor(private val stringHelper: IStringHelper, private val dataStoreHelper: IDataStoreHelper, cafeRepo: CafeRepo) : BaseViewModel<AddressListNavigator>() {

    override var navigator: WeakReference<AddressListNavigator>? = null

    private val cafeListLiveData = cafeRepo.cafeListLiveData
    val cafeAddressListLiveData = Transformations.map(cafeListLiveData) { cafeList ->
        cafeList.map { cafe ->
            cafe.address
        }
    }

    fun setCafe(address: Address) {
        val cafeId = cafeListLiveData.value?.find { cafe ->
            cafe.address.id == address.id
        }?.cafeEntity?.id
        cafeId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                dataStoreHelper.saveCafeId(it)
            }
        }
    }
}