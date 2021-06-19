package com.bunbeauty.fooddeliveryadmin.presentation.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.State
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

abstract class StatisticAddressListViewModel : BaseViewModel() {

    abstract val addressListState: StateFlow<State<List<AddressItem>>>

    abstract fun getCafeList()
}

class StatisticAddressListViewModelImpl @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val resourcesProvider: IResourcesProvider
) : StatisticAddressListViewModel() {

    override val addressListState = MutableStateFlow<State<List<AddressItem>>>(State.Loading())

    override fun getCafeList() {
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
            addressListState.value = addressItemList.toStateSuccess()
        }.launchIn(viewModelScope)
    }
}