package com.bunbeauty.presentation.feature.category.createcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.designsystem.compose.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_create_category_duplicate_name
import fooddeliveryadmin.presentation.generated.resources.error_common_create_category_empty_name
import fooddeliveryadmin.presentation.generated.resources.error_common_something_went_wrong
import org.jetbrains.compose.resources.StringResource

@Immutable
data class CreateCategoryViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val isLoading: Boolean,
            val nameField: TextFieldUi,
        ) : State
    }
}

@Composable
internal fun CreateCategoryState.DataState.toViewState(): CreateCategoryViewState =
    CreateCategoryViewState(
        state =
            when (state) {
                CreateCategoryState.DataState.State.LOADING -> CreateCategoryViewState.State.Loading
                CreateCategoryState.DataState.State.ERROR -> CreateCategoryViewState.State.Error
                CreateCategoryState.DataState.State.SUCCESS ->
                    CreateCategoryViewState.State.Success(
                        isLoading = isLoading,
                        nameField =
                            TextFieldUi(
                                value = nameField.value,
                                isError = nameField.isError,
                                errorResId =
                                    when (nameStateError) {
                                        CreateCategoryState.DataState.NameStateError.EMPTY_NAME ->
                                            Res.string.error_common_create_category_empty_name

                                        CreateCategoryState.DataState.NameStateError.DUPLICATE_NAME ->
                                            Res.string.error_common_create_category_duplicate_name

                                        CreateCategoryState.DataState.NameStateError.NO_ERROR ->
                                            Res.string.error_common_something_went_wrong
                                    },
                            ),
                    )
            },
    )
