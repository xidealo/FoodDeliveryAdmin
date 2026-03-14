package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class SelectAdditionListViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val selectedAdditionList: ImmutableList<AdditionItem>,
            val notSelectedAdditionList: ImmutableList<AdditionItem>,
            val groupName: String,
            val emptySelectedList: Boolean,
        ) : State

        data class SuccessDragDrop(
            val selectedAdditionList: ImmutableList<AdditionItem>,
            val groupName: String,
        ) : State
    }

    @Immutable
    data class AdditionItem(
        val uuid: String,
        val name: String,
    )
}

@Composable
internal fun SelectAdditionList.DataState.toViewState(): SelectAdditionListViewState =
    SelectAdditionListViewState(
        state =
            when (state) {
                SelectAdditionList.DataState.State.LOADING -> SelectAdditionListViewState.State.Loading
                SelectAdditionList.DataState.State.ERROR -> SelectAdditionListViewState.State.Error
                SelectAdditionList.DataState.State.SUCCESS ->
                    SelectAdditionListViewState.State.Success(
                        selectedAdditionList =
                            selectedAdditionList
                                .map {
                                    SelectAdditionListViewState.AdditionItem(
                                        uuid = it.uuid,
                                        name = it.name,
                                    )
                                }.toPersistentList(),
                        notSelectedAdditionList =
                            notSelectedAdditionList
                                .map {
                                    SelectAdditionListViewState.AdditionItem(
                                        uuid = it.uuid,
                                        name = it.name,
                                    )
                                }.toPersistentList(),
                        groupName = groupName,
                        emptySelectedList = emptySelectedList,
                    )

                SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP ->
                    SelectAdditionListViewState.State.SuccessDragDrop(
                        selectedAdditionList =
                            selectedAdditionList
                                .map {
                                    SelectAdditionListViewState.AdditionItem(
                                        uuid = it.uuid,
                                        name = it.name,
                                    )
                                }.toPersistentList(),
                        groupName = groupName,
                    )
            },
    )
