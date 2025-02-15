package com.bunbeauty.presentation.feature.cafelist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.cafelist.GetCafeWithWorkingHoursListFlowUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CafeListViewModel(
    private val getCafeWithWorkingHoursListFlow: GetCafeWithWorkingHoursListFlowUseCase
) : BaseViewModel() {

    private val mutableDataState = MutableStateFlow(
        CafeListDataState(
            state = CafeListDataState.State.LOADING
        )
    )
    val dataState = mutableDataState.asStateFlow()

    init {
        updateCafeList()
    }

    fun updateCafeList() {
        mutableDataState.update { state ->
            state.copy(state = CafeListDataState.State.LOADING)
        }
        viewModelScope.launchSafe(
            onError = {
                mutableDataState.update { state ->
                    state.copy(state = CafeListDataState.State.ERORR)
                }
            },
            block = {
                getCafeWithWorkingHoursListFlow().collect { cafeList ->
                    mutableDataState.update { state ->
                        state.copy(
                            state = CafeListDataState.State.SUCCESS,
                            cafeList = cafeList
                        )
                    }
                }
            }
        )
    }
}
