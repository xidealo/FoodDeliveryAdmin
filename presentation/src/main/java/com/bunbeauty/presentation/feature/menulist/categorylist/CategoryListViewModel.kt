package com.bunbeauty.presentation.feature.menulist.categorylist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.addmenuproduct.GetCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val SELECTED_CATEGORY_UUID_LIST = "selectedCategoryUuidList"

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoryListUseCase: GetCategoryListUseCase
) :
    BaseStateViewModel<CategoryList.DataState, CategoryList.Action, CategoryList.Event>(
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
                    dataState.getSelectedCategory()
                        .map { selectableCategory -> selectableCategory.category.uuid }
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
                        selectableCategoryList = getCategoryListUseCase().map { category ->
                            CategoryList.DataState.SelectableCategory(
                                category = category,
                                selected = false
                            )
                        }.map { selectableCategory ->
                            selectableCategory.copy(
                                selected = selectedCategoryUuidList.contains(
                                    selectableCategory.category.uuid
                                )
                            )
                        }
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
