package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
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
    private val stringUtil: IStringUtil,
    private val dataStoreRepo: DataStoreRepo,
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
                        stringUtil.toString(cafe.address),
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
            dataStoreRepo.saveCafeId(cafeId)
        }
    }
}