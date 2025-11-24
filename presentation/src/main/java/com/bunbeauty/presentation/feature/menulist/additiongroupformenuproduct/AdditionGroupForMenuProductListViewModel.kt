package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.feature.additionlist.SaveAdditionGroupForMenuProductListPriorityUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.GetAdditionGroupListFromMenuProductUseCase
import com.bunbeauty.domain.model.additiongroup.AdditionGroupForMenuProduct
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import kotlin.Int

class AdditionGroupForMenuProductListViewModel(
    private val getAdditionGroupListFromMenuProductUseCase: GetAdditionGroupListFromMenuProductUseCase,
    private val getAdditionListNameUseCase: GetAdditionListNameUseCase,
    private val saveAdditionGroupForMenuProductListUseCase: SaveAdditionGroupForMenuProductListPriorityUseCase,
) : BaseStateViewModel<
        AdditionGroupForMenuProductList.DataState,
        AdditionGroupForMenuProductList.Action,
        AdditionGroupForMenuProductList.Event,
    >(
        initState =
            AdditionGroupForMenuProductList.DataState(
                additionGroupList = listOf(),
                state = AdditionGroupForMenuProductList.DataState.State.LOADING,
                isRefreshing = false,
                emptyListAdditionGroup = false,
            ),
    ) {
    override fun reduce(
        action: AdditionGroupForMenuProductList.Action,
        dataState: AdditionGroupForMenuProductList.DataState,
    ) {
        when (action) {
            is AdditionGroupForMenuProductList.Action.Init ->
                loadData(
                    menuProductUuid = action.menuProductUuid,
                )

            is AdditionGroupForMenuProductList.Action.OnAdditionGroupClick ->
                onAdditionGroupClick(
                    additionGroupUuid = action.uuid,
                )

            AdditionGroupForMenuProductList.Action.OnBackClick -> backClick()
            AdditionGroupForMenuProductList.Action.OnCreateClick -> onCreateClick()
            is AdditionGroupForMenuProductList.Action.RefreshData ->
                refreshData(
                    menuProductUuid = action.menuProductUuid,
                )

            is AdditionGroupForMenuProductList.Action.MoveSelectedItem ->
                moveSelectedItem(
                    action.fromIndex,
                    action.toIndex,
                )

            AdditionGroupForMenuProductList.Action.OnCancelClicked -> cancelEditPriority()
            AdditionGroupForMenuProductList.Action.OnPriorityEditClicked -> onEditPriorityClicked()
            is AdditionGroupForMenuProductList.Action.OnSaveEditPriorityClick ->
                saveAdditionGroupForMenuProductListDrop(
                    additionGroupWithAdditions = action.updateAdditionGroupForMenuProductList,
                )
        }
    }

    private fun loadData(menuProductUuid: String) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.LOADING,
                        isRefreshing = false,
                    )
                }

                val additionGroupList =
                    getAdditionGroupListFromMenuProductUseCase(
                        menuProductUuid = menuProductUuid,
                    ).map { additionGroupList ->
                        AdditionGroupForMenuProduct(
                            uuid = additionGroupList.additionGroup.uuid,
                            name = additionGroupList.additionGroup.name,
                            additionNameList =
                                getAdditionListNameUseCase(
                                    additionList = additionGroupList.additionList,
                                ),
                            priority = additionGroupList.additionGroup.priority,
                        )
                    }

                setState {
                    copy(
                        additionGroupList = additionGroupList,
                        state = AdditionGroupForMenuProductList.DataState.State.SUCCESS,
                        emptyListAdditionGroup = additionGroupList.isEmpty(),
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun refreshData(menuProductUuid: String) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isRefreshing = true,
                    )
                }
                val additionGroupList =
                    getAdditionGroupListFromMenuProductUseCase(
                        menuProductUuid = menuProductUuid,
                    ).map { additionGroupList ->
                        AdditionGroupForMenuProduct(
                            uuid = additionGroupList.additionGroup.uuid,
                            name = additionGroupList.additionGroup.name,
                            additionNameList =
                                getAdditionListNameUseCase(
                                    additionList = additionGroupList.additionList,
                                ),
                            priority = additionGroupList.additionGroup.priority,
                        )
                    }

                setState {
                    copy(
                        additionGroupList = additionGroupList,
                        state = AdditionGroupForMenuProductList.DataState.State.SUCCESS,
                        isRefreshing = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun moveSelectedItem(
        fromIndex: Int,
        toIndex: Int,
    ) {
        setState {
            val mutableList = additionGroupList.toMutableList()

            val item = mutableList.removeAt(fromIndex)

            mutableList.add(toIndex, item)

            copy(additionGroupList = mutableList)
        }
    }

    private fun onAdditionGroupClick(additionGroupUuid: String) {
        sendEvent {
            AdditionGroupForMenuProductList.Event.OnAdditionGroupClicked(
                additionGroupUuid = additionGroupUuid,
            )
        }
    }

    private fun backClick() {
        sendEvent {
            AdditionGroupForMenuProductList.Event.Back
        }
    }

    private fun onCreateClick() {
        sendEvent {
            AdditionGroupForMenuProductList.Event.OnCreateClicked
        }
    }

    private fun cancelEditPriority() {
        setState {
            copy(
                state = AdditionGroupForMenuProductList.DataState.State.SUCCESS,
            )
        }
    }

    private fun onEditPriorityClicked() {
        setState {
            copy(
                state = AdditionGroupForMenuProductList.DataState.State.SUCCESS_DRAG_DROP,
            )
        }
    }

    private fun saveAdditionGroupForMenuProductListDrop(additionGroupWithAdditions: List<AdditionGroupForMenuProduct>) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.LOADING,
                    )
                }
                val newPriorityAdditionGroupForMenuProductList =
                    updatedPrioritiesItem(additionGroupWithAdditions)
                saveAdditionGroupForMenuProductListUseCase(
                    additionGroupList = newPriorityAdditionGroupForMenuProductList,
                )
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.SUCCESS,
                    )
                }
                sendEvent { AdditionGroupForMenuProductList.Event.ShowUpdateAdditionGroupListSuccess }
            },
            onError = {
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun updatedPrioritiesItem(additionGroupWithAdditions: List<AdditionGroupForMenuProduct>): List<String> =
        additionGroupWithAdditions.map { additionGroup ->
            additionGroup.uuid
        }
}
