package com.bunbeauty.presentation.feature.menulist.categorylist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetSelectableCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val SELECTED_CATEGORY_UUID_LIST = "selectedCategoryUuidList"

class CategoryListViewModel(
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase,
    savedStateHandle: SavedStateHandle
) : BaseStateViewModel<CategoryList.DataState, CategoryList.Action, CategoryList.Event>(
    initState = CategoryList.DataState(
        selectableCategoryList = listOf(),
        hasError = false
    )
) {

    private val selectedCategoryUuidList: List<String> = savedStateHandle.get<Array<String>>(
        SELECTED_CATEGORY_UUID_LIST
    )?.toList() ?: emptyList()

    override fun reduce(action: CategoryList.Action, dataState: CategoryList.DataState) {
        when (action) {
            CategoryList.Action.Init -> loadData()
            CategoryList.Action.OnBackClick -> sendEvent {
                CategoryList.Event.Back
            }

            CategoryList.Action.OnSaveClick -> sendEvent {
                CategoryList.Event.Save(
                    dataState.selectedCategoryList.map { selectableCategory ->
                        selectableCategory.category.uuid
                    }
                )
            }

            is CategoryList.Action.OnCategoryClick -> selectCategory(
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
