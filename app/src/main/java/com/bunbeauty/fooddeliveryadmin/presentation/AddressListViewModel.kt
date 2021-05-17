package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.toStateSuccess
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.fooddeliveryadmin.ui.adapter.AddressItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class AddressListViewModel : BaseViewModel() {

    abstract val addressListState: StateFlow<State<List<AddressItem>>>

    abstract fun saveCafeId(cafeId: String?)
}

class AddressListViewModelImpl @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val stringHelper: IStringHelper,
    private val dataStoreHelper: IDataStoreHelper,
) : AddressListViewModel() {

    override val addressListState = MutableStateFlow<State<List<AddressItem>>>(State.Loading())

    init {
        getCafeList()
    }

    fun getCafeList() {
        cafeRepo.cafeList.onEach { cafeList ->
            val addressItemList = ArrayList(
                cafeList.map { cafe ->
                    AddressItem(
                        stringHelper.toString(cafe.address),
                        cafe.cafeEntity.id
                    )
                }
            )
            addressListState.value = addressItemList.toStateSuccess()
        }.launchIn(viewModelScope)
    }

    override fun saveCafeId(cafeId: String?) {
        if (cafeId == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            dataStoreHelper.saveCafeId(cafeId)
        }
    }
}