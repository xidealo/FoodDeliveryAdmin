package com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.designsystem.compose.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_edit_addition_group_duplicate_name
import fooddeliveryadmin.presentation.generated.resources.error_common_edit_addition_group_empty_name
import fooddeliveryadmin.presentation.generated.resources.error_common_something_went_wrong

@Immutable
data class EditAdditionGroupViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val isLoading: Boolean,
            val isVisible: Boolean,
            val isVisibleSingleChoice: Boolean,
            val nameField: TextFieldUi,
        ) : State
    }


}

@Composable
internal fun EditAdditionGroupDataState.DataState.toViewState(): EditAdditionGroupViewState =
    EditAdditionGroupViewState(
        state =
            when (state) {
                EditAdditionGroupDataState.DataState.State.LOADING -> EditAdditionGroupViewState.State.Loading
                EditAdditionGroupDataState.DataState.State.ERROR -> EditAdditionGroupViewState.State.Error
                EditAdditionGroupDataState.DataState.State.SUCCESS ->
                    EditAdditionGroupViewState.State.Success(
                        isLoading = isLoading,
                        isVisible = isVisible,
                        isVisibleSingleChoice = isSingleChoice,
                        nameField =
                            TextFieldUi(
                                value = name.value,
                                isError = name.isError,
                                errorResId =
                                    when (nameStateError) {
                                        EditAdditionGroupDataState.DataState.NameStateError.EMPTY_NAME ->
                                            Res.string.error_common_edit_addition_group_empty_name

                                        EditAdditionGroupDataState.DataState.NameStateError.DUPLICATE_NAME ->
                                            Res.string.error_common_edit_addition_group_duplicate_name

                                        EditAdditionGroupDataState.DataState.NameStateError.NO_ERROR ->
                                            Res.string.error_common_something_went_wrong
                                    },
                            ),
                    )
            },
    )
