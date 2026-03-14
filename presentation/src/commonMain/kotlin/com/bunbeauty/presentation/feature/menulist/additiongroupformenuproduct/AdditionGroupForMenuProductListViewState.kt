package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.model.additiongroup.AdditionGroupForMenuProduct
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class AdditionGroupForMenuProductListViewState(
    val state: State,
    val isRefreshing: Boolean,
    val emptyListAdditionGroup: Boolean,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val additionGroupWithAdditionsList: ImmutableList<AdditionGroupForMenuProduct>,
        ) : State

        data class SuccessDragDrop(
            val additionGroupWithAdditionsList: ImmutableList<AdditionGroupForMenuProduct>,
        ) : State
    }
}

@Composable
internal fun AdditionGroupForMenuProductList.DataState.toViewState(): AdditionGroupForMenuProductListViewState =
    AdditionGroupForMenuProductListViewState(
        state =
            when (state) {
                AdditionGroupForMenuProductList.DataState.State.LOADING -> AdditionGroupForMenuProductListViewState.State.Loading
                AdditionGroupForMenuProductList.DataState.State.ERROR -> AdditionGroupForMenuProductListViewState.State.Error
                AdditionGroupForMenuProductList.DataState.State.SUCCESS ->
                    AdditionGroupForMenuProductListViewState.State.Success(
                        additionGroupWithAdditionsList = additionGroupList.toPersistentList(),
                    )

                AdditionGroupForMenuProductList.DataState.State.SUCCESS_DRAG_DROP ->
                    AdditionGroupForMenuProductListViewState.State.SuccessDragDrop(
                        additionGroupWithAdditionsList = additionGroupList.toPersistentList(),
                    )
            },
        isRefreshing = isRefreshing,
        emptyListAdditionGroup = emptyListAdditionGroup,
    )
