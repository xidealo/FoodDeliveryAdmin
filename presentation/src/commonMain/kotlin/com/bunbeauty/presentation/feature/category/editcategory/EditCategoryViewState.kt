package com.bunbeauty.presentation.feature.category.editcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.designsystem.compose.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_create_category_duplicate_name
import fooddeliveryadmin.presentation.generated.resources.error_common_create_category_empty_name
import fooddeliveryadmin.presentation.generated.resources.error_common_something_went_wrong

@Immutable
data class EditCategoryViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val nameField: TextFieldUi,
            val isLoading: Boolean,
        ) : State
    }
}

@Composable
internal fun EditCategoryState.DataState.toViewState(): EditCategoryViewState =
    EditCategoryViewState(
        state =
            when (state) {
                EditCategoryState.DataState.State.LOADING -> EditCategoryViewState.State.Loading
                EditCategoryState.DataState.State.ERROR -> EditCategoryViewState.State.Error
                EditCategoryState.DataState.State.SUCCESS ->
                    EditCategoryViewState.State.Success(
                        nameField =
                            TextFieldUi(
                                value = name.value,
                                isError = name.isError,
                                errorResId =
                                    when (nameStateError) {
                                        EditCategoryState.DataState.NameStateError.EMPTY_NAME ->
                                            Res.string.error_common_create_category_empty_name

                                        EditCategoryState.DataState.NameStateError.DUPLICATE_NAME ->
                                            Res.string.error_common_create_category_duplicate_name

                                        EditCategoryState.DataState.NameStateError.NO_ERROR ->
                                            Res.string.error_common_something_went_wrong
                                    },
                            ),
                        isLoading = isLoading,
                    )
            },
    )
