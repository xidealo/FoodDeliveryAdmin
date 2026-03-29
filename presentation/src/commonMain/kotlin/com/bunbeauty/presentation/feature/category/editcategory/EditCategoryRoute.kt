package com.bunbeauty.presentation.feature.category.editcategory

import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_create_category_save
import fooddeliveryadmin.presentation.generated.resources.hint_edit_addition_name
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_edit_category_updated
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_edit_category
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditCategoryRouteScreen(
    viewModel: EditCategoryViewModel = koinViewModel(),
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: EditCategoryState.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(Unit) {
        onAction(EditCategoryState.Action.Init)
    }

    EditCategoryEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
    )

    EditCategoryScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun EditCategoryEffect(
    effects: List<EditCategoryState.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                EditCategoryState.Event.GoBackEvent -> {
                    goBack()
                }

                is EditCategoryState.Event.ShowUpdateCategorySuccess -> {
                    showInfoMessage(
                        getString(
                            Res.string.msg_edit_category_updated,
                            effect.categoryName,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun EditCategoryScreen(
    state: EditCategoryViewState,
    onAction: (EditCategoryState.Action) -> Unit,
    goBack: () -> Unit,
) {
    AdminScaffold(
        backgroundColor = AdminTheme.colors.main.surface,
        title = stringResource(Res.string.title_edit_category),
        backActionClick = {
            onAction(EditCategoryState.Action.OnBackClicked)
        },
    ) {
        when (state.state) {
            EditCategoryViewState.State.Loading -> {
                LoadingScreen()
            }

            EditCategoryViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(EditCategoryState.Action.Init)
                    },
                )
            }

            is EditCategoryViewState.State.Success -> {
                EditCategorySuccessScreen(state = state.state, onAction = onAction)
            }
        }
    }
}

@Composable
private fun EditCategorySuccessScreen(
    state: EditCategoryViewState.State.Success,
    onAction: (EditCategoryState.Action) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        AdminTextField(
            modifier = Modifier.fillMaxWidth(),
            labelText = stringResource(Res.string.hint_edit_addition_name),
            value = state.nameField.value,
            onValueChange = { name ->
                onAction(
                    EditCategoryState.Action.EditNameCategory(name),
                )
            },
            errorText = state.nameField.errorResId,
            isError = state.nameField.isError,
            enabled = !state.isLoading,
        )
        Spacer(modifier = Modifier.weight(1f))

        LoadingButton(
            text = stringResource(Res.string.action_create_category_save),
            isLoading = state.isLoading,
            onClick = {
                onAction(EditCategoryState.Action.OnSaveEditCategoryClick)
            },
        )
    }
}
