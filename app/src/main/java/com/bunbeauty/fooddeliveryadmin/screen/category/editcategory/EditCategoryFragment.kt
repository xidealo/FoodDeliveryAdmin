package com.bunbeauty.fooddeliveryadmin.screen.category.editcategory

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.feature.category.editcategory.EditCategoryState
import com.bunbeauty.presentation.feature.category.editcategory.EditCategoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditCategoryFragment :
    BaseComposeFragment<EditCategoryState.DataState, EditCategoryViewState, EditCategoryState.Action, EditCategoryState.Event>() {

    override val viewModel: EditCategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(EditCategoryState.Action.Init)
    }

    @Composable
    override fun Screen(
        state: EditCategoryViewState,
        onAction: (EditCategoryState.Action) -> Unit
    ) {
        EditCategoriesScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    private fun EditCategoriesScreen(
        state: EditCategoryViewState,
        onAction: (EditCategoryState.Action) -> Unit
    ) {
        AdminScaffold(
            backgroundColor = AdminTheme.colors.main.surface,
            title = stringResource(R.string.title_edit_category),
            backActionClick = {
                onAction(EditCategoryState.Action.OnBackClicked)
            }
        ) {
            when (state.state) {
                EditCategoryViewState.State.Loading -> {
                    LoadingScreen()
                }

                EditCategoryViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(EditCategoryState.Action.Init)
                        }
                    )
                }

                is EditCategoryViewState.State.Success -> {
                    EditCategoriesSuccessScreen(state = state.state, onAction = onAction)
                }
            }
        }
    }

    @Composable
    private fun EditCategoriesSuccessScreen(
        state: EditCategoryViewState.State.Success,
        onAction: (EditCategoryState.Action) -> Unit
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(R.string.hint_edit_addition_name),
                value = state.nameField.value,
                onValueChange = { name ->
                    onAction(
                        EditCategoryState.Action.EditNameCategory(name)
                    )
                },
                errorText = stringResource(state.nameField.errorResId),
                isError = state.nameField.isError,
                enabled = !state.isLoading

            )
            Spacer(modifier = Modifier.weight(1f))

            LoadingButton(
                text = stringResource(R.string.action_create_category_save),
                isLoading = state.isLoading,
                onClick = {
                    onAction(EditCategoryState.Action.OnSaveEditCategoryClick)
                }
            )
        }
    }

    @Composable
    override fun mapState(state: EditCategoryState.DataState): EditCategoryViewState {
        return EditCategoryViewState(
            state = when (state.state) {
                EditCategoryState.DataState.State.LOADING -> EditCategoryViewState.State.Loading
                EditCategoryState.DataState.State.ERROR -> EditCategoryViewState.State.Error
                EditCategoryState.DataState.State.SUCCESS -> EditCategoryViewState.State.Success(
                    isLoading = state.isLoading,
                    nameField = TextFieldUi(
                        value = state.name.value,
                        isError = state.name.isError,
                        errorResId = when (state.nameStateError) {
                            EditCategoryState.DataState.NameStateError.EMPTY_NAME ->
                                R.string.error_common_create_category_empty_name

                            EditCategoryState.DataState.NameStateError.DUPLICATE_NAME ->
                                R.string.error_common_create_category_duplicate_name

                            EditCategoryState.DataState.NameStateError.NO_ERROR ->
                                R.string.error_common_something_went_wrong
                        }
                    )
                )
            }
        )
    }

    override fun handleEvent(event: EditCategoryState.Event) {
        when (event) {
            EditCategoryState.Event.GoBackEvent -> {
                findNavController().navigateUp()
            }

            is EditCategoryState.Event.ShowUpdateCategorySuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_edit_category_updated, event.categoryName)
                )
                findNavController().popBackStack()
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun EditCategoryPreview() {
        AdminTheme {
            EditCategoriesScreen(
                state = EditCategoryViewState(
                    state = EditCategoryViewState.State.Success(
                        nameField = TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = 0
                        ),
                        isLoading = false
                    )

                ),
                onAction = {}
            )
        }
    }
}
