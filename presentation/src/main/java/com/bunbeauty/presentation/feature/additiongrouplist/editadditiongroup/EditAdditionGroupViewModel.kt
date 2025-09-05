package com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val ADDITION_GROUP_UUID = "additionGroupUuid"

class EditAdditionGroupViewModel(
    private val savedStateHandle: SavedStateHandle,
   // private val getAdditionGroupUseCase: GetAdditionGroupUseCase,

): BaseStateViewModel<EditAdditionGroupDataState.DataState, EditAdditionGroupDataState.Action, EditAdditionGroupDataState.Event>(
    initState = EditAdditionGroupDataState.DataState(
        uuid = "",
        name = TextFieldData.empty,
        isLoading = false,
        state = EditAdditionGroupDataState.DataState.State.SUCCESS,
        nameStateError = EditAdditionGroupDataState.DataState.NameStateError.NO_ERROR,
        isVisible = true,
        isVisibleSingleChoice = true
    )
) {
    override fun reduce(
        action: EditAdditionGroupDataState.Action,
        dataState: EditAdditionGroupDataState.DataState
    ) {
        when(action){
            EditAdditionGroupDataState.Action.Init -> TODO()
            EditAdditionGroupDataState.Action.OnBackClicked -> onBackClicked()
            is EditAdditionGroupDataState.Action.EditNameAdditionGroup -> TODO()
            EditAdditionGroupDataState.Action.OnSaveEditAdditionGroupClick -> TODO()
            is EditAdditionGroupDataState.Action.OnVisibleMenu -> TODO()
            is EditAdditionGroupDataState.Action.OnVisibleSingleChoice -> TODO()
        }
    }

    private fun onBackClicked() {
        sendEvent {
            EditAdditionGroupDataState.Event.GoBackEvent
        }
    }

//    private fun loadData() {
//        viewModelScope.launchSafe(
//            block = {
//                val additionGroupUuidNavigation =
//                    savedStateHandle.get<String>(ADDITION_GROUP_UUID).orEmpty()
//                val additionGroup = getAdditionGroupUseCase(additionGroupUuid = additionGroupUuidNavigation)
//                setState {
//                    copy(
//                        uuid = additionGroup.uuid,
//                        name = name.copy(
//                            value = additionGroup.name,
//                            isError = false
//                        )
//                    )
//                }
//            },
//            onError = {
//                // No errors
//            }
//        )
//    }
}