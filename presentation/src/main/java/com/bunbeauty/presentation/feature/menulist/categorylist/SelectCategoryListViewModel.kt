package com.bunbeauty.presentation.feature.menulist.categorylist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetSelectableCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val SELECTED_CATEGORY_UUID_LIST = "selectedCategoryUuidList"

class SelectCategoryListViewModel(
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase,
    savedStateHandle: SavedStateHandle
) : BaseStateViewModel<SelectCategoryList.DataState, SelectCategoryList.Action, SelectCategoryList.Event>(
    initState = SelectCategoryList.DataState(
        selectableCategoryList = listOf(),
        hasError = false
    )
) {

    private val selectedCategoryUuidList: List<String> = savedStateHandle.get<Array<String>>(
        SELECTED_CATEGORY_UUID_LIST
    )?.toList() ?: emptyList()

    override fun reduce(
        action: SelectCategoryList.Action,
        dataState: SelectCategoryList.DataState
    ) {
        when (action) {
            SelectCategoryList.Action.Init -> loadData()
            SelectCategoryList.Action.OnBackClick -> sendEvent {
                SelectCategoryList.Event.Back
            }

            SelectCategoryList.Action.OnSaveClick -> sendEvent {
                SelectCategoryList.Event.Save(
                    dataState.selectedCategoryList.map { selectableCategory ->
                        selectableCategory.category.uuid
                    }
                )
            }

            is SelectCategoryList.Action.OnCategoryClick -> selectCategory(
                uuid = action.uuid,
                selected = action.selected
            )
        }
    }

    private fun selectCategory(uuid: String, selected: Boolean) {
        setState {
            copy(
                selectableCategoryList = selectableCategoryList.map { selectableCategory ->
                    if (uuid == selectableCategory.category.uuid) {
                        selectableCategory.copy(selected = !selected)
                    } else {
                        selectableCategory
                    }
                }
            )
        }
    }


    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        selectableCategoryList = getSelectableCategoryListUseCase(
                            selectedCategoryUuidList = selectedCategoryUuidList
                        )
                    )
                }
            },
            onError = {
                setState {
                    copy(hasError = true)
                }
            }
        )
    }
}
