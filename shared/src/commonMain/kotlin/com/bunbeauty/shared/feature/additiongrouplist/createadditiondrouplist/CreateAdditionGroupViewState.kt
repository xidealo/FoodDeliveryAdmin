package com.bunbeauty.shared.feature.additiongrouplist.createadditiondrouplist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_common_create_addition_group_duplicate_name
import fooddeliveryadmin.shared.generated.resources.error_common_create_addition_group_name
import fooddeliveryadmin.shared.generated.resources.error_common_something_went_wrong

@Immutable
data class CreateAdditionGroupViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val isLoading: Boolean,
            val nameField: TextFieldUi,
            val singleChoice: Boolean,
            val isShowMenuVisible: Boolean,
        ) : State
    }
}

@Composable
internal fun CreateAdditionGroupDataState.DataState.toViewState(): CreateAdditionGroupViewState =
    CreateAdditionGroupViewState(
        state =
            when (state) {
                CreateAdditionGroupDataState.DataState.State.SUCCESS ->
                    CreateAdditionGroupViewState.State.Success(
                        isLoading = isLoading,
                        nameField =
                            TextFieldUi(
                                value = nameField.value,
                                isError = nameField.isError,
                                errorResId =
                                    when (nameStateError) {
                                        CreateAdditionGroupDataState.DataState.NameStateError.EMPTY_NAME ->
                                            Res.string.error_common_create_addition_group_name

                                        CreateAdditionGroupDataState.DataState.NameStateError.DUPLICATE_NAME ->
                                            Res.string.error_common_create_addition_group_duplicate_name

                                        CreateAdditionGroupDataState.DataState.NameStateError.NO_ERROR ->
                                            Res.string.error_common_something_went_wrong
                                    },
                            ),
                        singleChoice = singleChoice,
                        isShowMenuVisible = isShowMenuVisible,
                    )

                CreateAdditionGroupDataState.DataState.State.ERROR ->
                    CreateAdditionGroupViewState.State.Error

                CreateAdditionGroupDataState.DataState.State.LOADING ->
                    CreateAdditionGroupViewState.State.Loading
            },
    )
