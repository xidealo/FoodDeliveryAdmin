package com.bunbeauty.fooddeliveryadmin.presentation.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StatisticAddressListViewModel @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val resourcesProvider: IResourcesProvider
) : BaseViewModel() {

    val addressListState: StateFlow<State<List<AddressItem>>>
        get() = _addressListState
    private val _addressListState = MutableStateFlow<State<List<AddressItem>>>(State.Loading())

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
            addressItemList.add(
                AddressItem(
                    resourcesProvider.getString(R.string.msg_statistic_all_cafes),
                    null
                )
            )
            _addressListState.value = addressItemList.toStateSuccess()
        }.launchIn(viewModelScope)
    }
}