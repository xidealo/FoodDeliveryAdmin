package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class EditAdditionGroupForMenuProductViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val groupName: String?,
            val additionNameList: String?,
            val additionGroupForMenuProductUuid: String,
            val menuProductUuid: String,
            val editedAdditionGroupUuid: String?,
            val editedAdditionListUuid: List<String>?,
        ) : State
    }
}

@Immutable
@Composable
internal fun EditAdditionGroupForMenu.DataState.toViewState(): EditAdditionGroupForMenuProductViewState =
    EditAdditionGroupForMenuProductViewState(
        state =
            when (state) {
                EditAdditionGroupForMenu.DataState.State.LOADING -> EditAdditionGroupForMenuProductViewState.State.Loading
                EditAdditionGroupForMenu.DataState.State.ERROR -> EditAdditionGroupForMenuProductViewState.State.Error
                EditAdditionGroupForMenu.DataState.State.SUCCESS ->
                    EditAdditionGroupForMenuProductViewState.State.Success(
                        groupName = groupName,
                        additionNameList = additionNameList,
                        additionGroupForMenuProductUuid = additionGroupForMenuProductUuid,
                        menuProductUuid = menuProductUuid,
                        editedAdditionGroupUuid = editedAdditionGroupUuid,
                        editedAdditionListUuid = editedAdditionListUuid?.toPersistentList(),
                    )
            },
    )
