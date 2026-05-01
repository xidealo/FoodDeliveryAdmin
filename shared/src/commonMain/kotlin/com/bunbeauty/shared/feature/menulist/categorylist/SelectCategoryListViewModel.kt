package com.bunbeauty.shared.feature.menulist.categorylist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetSelectableCategoryListUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class SelectCategoryListViewModel(
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase,
) : BaseStateViewModel<SelectCategoryList.DataState, SelectCategoryList.Action, SelectCategoryList.Event>(
        initState =
            SelectCategoryList.DataState(
                selectableCategoryList = listOf(),
                hasError = false,
            ),
    ) {
    override fun reduce(
        action: SelectCategoryList.Action,
        dataState: SelectCategoryList.DataState,
    ) {
        when (action) {
            is SelectCategoryList.Action.Init ->
                loadData(
                    selectedCategoryList = action.selectedCategoryList,
                )

            SelectCategoryList.Action.OnBackClick ->
                sendEvent {
                    SelectCategoryList.Event.Back
                }

            SelectCategoryList.Action.OnSaveClick -> {
                val list =
                    dataState.selectedCategoryList.map { selectableCategory ->
                        selectableCategory.category.uuid
                    }

                sendEvent {
                    SelectCategoryList.Event.Save(
                        selectedCategoryUuidList = list,
                    )
                }
            }

            is SelectCategoryList.Action.OnCategoryClick ->
                selectCategory(
                    uuid = action.uuid,
                    selected = action.selected,
                )
        }
    }

    private fun selectCategory(
        uuid: String,
        selected: Boolean,
    ) {
        setState {
            copy(
                selectableCategoryList =
                    selectableCategoryList.map { selectableCategory ->
                        if (uuid == selectableCategory.category.uuid) {
                            selectableCategory.copy(selected = !selected)
                        } else {
                            selectableCategory
                        }
                    },
            )
        }
    }

    private fun loadData(selectedCategoryList: List<String>) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        selectableCategoryList =
                            getSelectableCategoryListUseCase(
                                selectedCategoryUuidList = selectedCategoryList,
                            ),
                    )
                }
            },
            onError = {
                setState {
                    copy(hasError = true)
                }
            },
        )
    }
}
