package com.bunbeauty.fooddeliveryadmin.screen.category.createcategory

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
import com.bunbeauty.presentation.feature.category.createcategory.CreateCategoryState
import com.bunbeauty.presentation.feature.category.createcategory.CreateCategoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateCategoryFragment() :
    BaseComposeFragment<CreateCategoryState.DataState, CreateCategoryViewState, CreateCategoryState.Action, CreateCategoryState.Event>() {

    override val viewModel: CreateCategoryViewModel by viewModel()

    @Composable
    override fun Screen(
        state: CreateCategoryViewState,
        onAction: (CreateCategoryState.Action) -> Unit
    ) {
        CreateCategoryScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    override fun mapState(state: CreateCategoryState.DataState): CreateCategoryViewState {
        return CreateCategoryViewState(
            state = when (state.state) {
                CreateCategoryState.DataState.State.LOADING -> CreateCategoryViewState.State.Loading
                CreateCategoryState.DataState.State.ERROR -> CreateCategoryViewState.State.Error
                CreateCategoryState.DataState.State.SUCCESS -> CreateCategoryViewState.State.Success(
                    isLoading = state.isLoading,
                    nameField = TextFieldUi(
                        value = state.nameField.value,
                        isError = state.hasCreateNameError || state.hasDuplicateNameError,
                        errorResId = when {
                            state.hasDuplicateNameError -> {
                                R.string.error_common_create_category_duplicate_name
                            }
                            else -> {
                                R.string.error_common_create_category_empty_name
                            }
                        }
                    )
                )
            }
        )
    }

    override fun handleEvent(event: CreateCategoryState.Event) {
        when (event) {
            CreateCategoryState.Event.GoBackEvent -> findNavController().navigateUp()

            is CreateCategoryState.Event.ShowUpdateCategorySuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_create_category_updated, event.categoryName)
                )
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    private fun CreateCategoryScreen(
        state: CreateCategoryViewState,
        onAction: (CreateCategoryState.Action) -> Unit
    ) {
        AdminScaffold(
            backgroundColor = AdminTheme.colors.main.surface,
            title = stringResource(R.string.title_create_category),
            backActionClick = {
                onAction(CreateCategoryState.Action.OnBackClicked)
            }
        ) {
            when (state.state) {
                CreateCategoryViewState.State.Error -> ErrorScreen(
                    mainTextId = R.string.title_common_can_not_load_data,
                    extraTextId = R.string.msg_common_check_connection_and_retry,
                    onClick = {}
                )

                CreateCategoryViewState.State.Loading -> LoadingScreen()
                is CreateCategoryViewState.State.Success -> CreateCategoryScreenSuccess(
                    state = state.state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    private fun CreateCategoryScreenSuccess(
        state: CreateCategoryViewState.State.Success,
        onAction: (CreateCategoryState.Action) -> Unit
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
                        CreateCategoryState.Action.CreateNameCategory(name)
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
                    onAction(CreateCategoryState.Action.OnSaveCreateCategoryClick)
                }
            )
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun CreateCategoryPreview() {
        AdminTheme {
            CreateCategoryScreen(
                state = CreateCategoryViewState(
                    state = CreateCategoryViewState.State.Success(
                        isLoading = false,
                        nameField = TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = 0
                        )
                    )

                ),
                onAction = {}
            )
        }
    }
}
