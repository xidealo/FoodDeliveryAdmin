package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val dataStoreRepo: DataStoreRepo,
) : BaseViewModel() {

    val addressListState: StateFlow<State<List<AddressItem>>>
        get() = _addressListState
    val _addressListState = MutableStateFlow<State<List<AddressItem>>>(State.Loading())

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
            _addressListState.value = addressItemList.toStateSuccess()
        }.launchIn(viewModelScope)
    }

    fun saveCafeId(cafeId: String?) {
        if (cafeId == null) {
            return
        }

        viewModelScope.launch(IO) {
            dataStoreRepo.saveCafeId(cafeId)
            withContext(Main) {
                goBack()
            }
        }
    }
}