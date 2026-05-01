package com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.viewmodel.base.BaseViewState

@Immutable
data class CreateAdditionGroupForMenuProductViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val groupName: String?,
            val additionNameList: String?,
            val menuProductUuid: String,
            val additionGroupForMenuProductUuid: String,
            val editedAdditionGroupUuid: String?,
            val createdAdditionListUuid: List<String>,
            val groupHasError: Boolean,
            val additionListHasError: Boolean,
            val isSaveLoading: Boolean,
        ) : State
    }
}

@Composable
internal fun CreateAdditionGroupForMenu.DataState.toViewState(): CreateAdditionGroupForMenuProductViewState =
    CreateAdditionGroupForMenuProductViewState(
        state =
            when (state) {
                CreateAdditionGroupForMenu.DataState.State.LOADING -> CreateAdditionGroupForMenuProductViewState.State.Loading
                CreateAdditionGroupForMenu.DataState.State.ERROR -> CreateAdditionGroupForMenuProductViewState.State.Error
                CreateAdditionGroupForMenu.DataState.State.SUCCESS ->
                    CreateAdditionGroupForMenuProductViewState.State.Success(
                        groupName = groupName,
                        additionNameList = additionNameList,
                        menuProductUuid = menuProductUuid,
                        additionGroupForMenuProductUuid = additionGroupForMenuProductUuid,
                        editedAdditionGroupUuid = editedAdditionGroupUuid,
                        createdAdditionListUuid = createdAdditionListUuid ?: emptyList(),
                        groupHasError = groupHasError,
                        additionListHasError = additionListHasError,
                        isSaveLoading = isSaveLoading,
                    )
            },
    )
