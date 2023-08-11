package com.bunbeauty.presentation.feature.cafe_list

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.cafelist.GetCafeWithWorkingHoursListFlowUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CafeListViewModel @Inject constructor(
    private val getCafeWithWorkingHoursListFlow: GetCafeWithWorkingHoursListFlowUseCase,
) : BaseViewModel() {

    private val mutableUiState = MutableStateFlow(CafeListUiState(CafeListUiState.State.Loading))
    val uiState = mutableUiState.asStateFlow()

    init {
        updateCafeList()
    }

    fun updateCafeList() {
        mutableUiState.update { state ->
            state.copy(
                state = CafeListUiState.State.Loading
            )
        }
        viewModelScope.launchSafe(
            onError = {
                mutableUiState.update { state ->
                    state.copy(state = CafeListUiState.State.Error)
                }
            },
            block = {
                getCafeWithWorkingHoursListFlow().collect {
                    mutableUiState.update { state ->
                        state.copy(
                            state = CafeListUiState.State.Success(it)
                        )
                    }
                }
            }
        )
    }

}