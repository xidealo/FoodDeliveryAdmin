package com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.selectadditiongroup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.SelectableAdditionGroup
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class SelectAdditionGroupViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data object Empty : State

        data class Success(
            val visibleSelectableAdditionGroupList: ImmutableList<SelectableAdditionGroup>,
            val hiddenSelectableAdditionGroupList: ImmutableList<SelectableAdditionGroup>,
        ) : State
    }
}

@Composable
internal fun SelectAdditionGroup.DataState.toViewState(): SelectAdditionGroupViewState =
    SelectAdditionGroupViewState(
        state =
            when (state) {
                SelectAdditionGroup.DataState.State.LOADING -> SelectAdditionGroupViewState.State.Loading
                SelectAdditionGroup.DataState.State.ERROR -> SelectAdditionGroupViewState.State.Error
                SelectAdditionGroup.DataState.State.SUCCESS ->
                    if (hasNoAvailableAdditionGroups) {
                        SelectAdditionGroupViewState.State.Empty
                    } else {
                        SelectAdditionGroupViewState.State.Success(
                            visibleSelectableAdditionGroupList = visibleSelectableAdditionGroupList.toPersistentList(),
                            hiddenSelectableAdditionGroupList = hiddenSelectableAdditionGroupList.toPersistentList(),
                        )
                    }
            },
    )
