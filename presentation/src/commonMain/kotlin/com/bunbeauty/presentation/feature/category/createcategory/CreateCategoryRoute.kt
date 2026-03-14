package com.bunbeauty.presentation.feature.category.createcategory

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
import fooddeliveryadmin.presentation.generated.resources.msg_create_category_created
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_create_category
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateCategoryRouteScreen(
    viewModel: CreateCategoryViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CreateCategoryState.Action ->
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

    CreateCategoryEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
    )

    CreateCategoryScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun CreateCategoryEffect(
    effects: List<CreateCategoryState.Event>,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CreateCategoryState.Event.GoBackEvent -> {
                    goBack()
                }

                is CreateCategoryState.Event.ShowUpdateCategorySuccess -> {
                    showInfoMessage(
                        getString(
                            Res.string.msg_create_category_created,
                            effect.categoryName,
                        ),
                        0,
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun CreateCategoryScreen(
    state: CreateCategoryViewState,
    onAction: (CreateCategoryState.Action) -> Unit,
    goBack: () -> Unit,
) {
    AdminScaffold(
        backgroundColor = AdminTheme.colors.main.surface,
        title = stringResource(Res.string.title_create_category),
        backActionClick = {
            onAction(CreateCategoryState.Action.OnBackClicked)
        },
    ) {
        when (state.state) {
            CreateCategoryViewState.State.Error ->
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(CreateCategoryState.Action.OnErrorStateClicked)
                    },
                )

            CreateCategoryViewState.State.Loading -> LoadingScreen()

            is CreateCategoryViewState.State.Success ->
                CreateCategoryScreenSuccess(
                    state = state.state,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun CreateCategoryScreenSuccess(
    state: CreateCategoryViewState.State.Success,
    onAction: (CreateCategoryState.Action) -> Unit,
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
                    CreateCategoryState.Action.CreateNameCategoryChanged(name),
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
                onAction(CreateCategoryState.Action.OnSaveCreateCategoryClick)
            },
        )
    }
}
