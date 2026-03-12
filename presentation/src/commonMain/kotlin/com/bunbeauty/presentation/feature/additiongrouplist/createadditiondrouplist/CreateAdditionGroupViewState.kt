package com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_create_addition_group_duplicate_name
import fooddeliveryadmin.presentation.generated.resources.error_common_create_addition_group_name
import fooddeliveryadmin.presentation.generated.resources.error_common_something_went_wrong
import org.jetbrains.compose.resources.StringResource

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

    @Immutable
    data class TextFieldUi(
        val value: String,
        val isError: Boolean,
        val errorResId: StringResource,
    )
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
                            CreateAdditionGroupViewState.TextFieldUi(
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
