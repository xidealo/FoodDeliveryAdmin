package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.card.NavigationTextCard
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.navigation.CreateAdditionGroupForMenuProductScreenDestination
import com.bunbeauty.presentation.navigation.NavStateHandleParameters.SELECTED_ADDITION_GROUP_UUID
import com.bunbeauty.presentation.navigation.NavStateHandleParameters.SELECTED_ADDITION_UUID_LIST
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_create_addition_group_for_menu_product_save
import fooddeliveryadmin.presentation.generated.resources.error_create_addition_group_for_menu_product_addition
import fooddeliveryadmin.presentation.generated.resources.error_create_addition_group_for_menu_product_group
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_update_create_addition_group_for_menu_product_addition
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_create_addition_group_for_menu_product
import fooddeliveryadmin.presentation.generated.resources.title_create_addition_group_for_menu_product_addition
import fooddeliveryadmin.presentation.generated.resources.title_create_addition_group_for_menu_product_group
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CreateAdditionGroupForMenuProductRouteScreen(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToSelectAdditionGroup: (String, String, String?) -> Unit,
    goToSelectAdditionList: (String?, String, String, List<String>) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val route = backStackEntry.toRoute<CreateAdditionGroupForMenuProductScreenDestination>()
    val viewModel: CreateAdditionGroupForMenuProductViewModel =
        koinViewModel(
            parameters = { parametersOf(route.menuProductUuid) },
        )

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CreateAdditionGroupForMenu.Action ->
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
        backStackEntry.savedStateHandle
            .getStateFlow(
                SELECTED_ADDITION_GROUP_UUID,
                viewState.editedAdditionGroupUuid,
            ).collect { selectedAdditionGroupUuid ->
                onAction(
                    CreateAdditionGroupForMenu.Action.SelectAdditionGroup(
                        additionGroupUuid = selectedAdditionGroupUuid,
                    ),
                )
            }
    }

    LaunchedEffect(Unit) {
        backStackEntry.savedStateHandle
            .getStateFlow(
                SELECTED_ADDITION_UUID_LIST,
                viewState.createdAdditionListUuid ?: emptyList(),
            ).collect { selectedAdditionUuidList ->
                onAction(
                    CreateAdditionGroupForMenu.Action.SelectAdditionList(
                        additionListUuid = selectedAdditionUuidList,
                    ),
                )
            }
    }

    CreateAdditionGroupForMenuEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
        goToSelectAdditionGroup = goToSelectAdditionGroup,
        goToSelectAdditionList = goToSelectAdditionList,
    )

    CreateAdditionGroupForMenuProductScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun CreateAdditionGroupForMenuEffect(
    effects: List<CreateAdditionGroupForMenu.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    goToSelectAdditionGroup: (String, String, String?) -> Unit,
    goToSelectAdditionList: (String?, String, String, List<String>) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CreateAdditionGroupForMenu.Event.Back -> {
                    goBack()
                }

                CreateAdditionGroupForMenu.Event.SaveAndBack -> {
                    showInfoMessage(
                        getString(
                            resource = Res.string.msg_update_create_addition_group_for_menu_product_addition,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                    goBack()
                }

                is CreateAdditionGroupForMenu.Event.OnAdditionGroupClick -> {
                    goToSelectAdditionGroup(
                        effect.uuid,
                        effect.menuProductUuid,
                        null,
                    )
                }

                is CreateAdditionGroupForMenu.Event.OnAdditionListClick -> {
                    goToSelectAdditionList(
                        null,
                        effect.menuProductUuid,
                        effect.additionGroupName,
                        effect.createEdAdditionListUuid,
                    )
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun CreateAdditionGroupForMenuProductScreen(
    state: CreateAdditionGroupForMenuProductViewState,
    onAction: (CreateAdditionGroupForMenu.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_create_addition_group_for_menu_product),
        backActionClick = {
            onAction(CreateAdditionGroupForMenu.Action.OnBackClick)
        },
        backgroundColor = AdminTheme.colors.main.surface,
    ) {
        when (state.state) {
            CreateAdditionGroupForMenuProductViewState.State.Loading -> LoadingScreen()

            CreateAdditionGroupForMenuProductViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(CreateAdditionGroupForMenu.Action.OnBackClick)
                    },
                )
            }

            is CreateAdditionGroupForMenuProductViewState.State.Success ->
                CreateAdditionGroupForMenuProductSuccessScreen(
                    state = state.state,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun CreateAdditionGroupForMenuProductSuccessScreen(
    state: CreateAdditionGroupForMenuProductViewState.State.Success,
    onAction: (CreateAdditionGroupForMenu.Action) -> Unit,
) {
    Column {
        NavigationTextCard(
            valueText = state.groupName,
            onClick = {
                onAction(
                    CreateAdditionGroupForMenu.Action.OnAdditionGroupClick,
                )
            },
            elevated = false,
            labelText =
                stringResource(
                    resource = Res.string.title_create_addition_group_for_menu_product_group,
                ),
            isError = state.groupHasError,
            errorText = Res.string.error_create_addition_group_for_menu_product_group,
            hasDivider = true,
        )

        NavigationTextCard(
            valueText = state.additionNameList,
            onClick = {
                onAction(
                    CreateAdditionGroupForMenu.Action.OnAdditionListClick,
                )
            },
            elevated = false,
            labelText =
                stringResource(
                    resource = Res.string.title_create_addition_group_for_menu_product_addition,
                ),
            isError = state.additionListHasError,
            errorText = Res.string.error_create_addition_group_for_menu_product_addition,
            hasDivider = true,
        )

        Spacer(modifier = Modifier.weight(1f))
        LoadingButton(
            modifier = Modifier.padding(16.dp),
            text = stringResource(Res.string.action_create_addition_group_for_menu_product_save),
            isLoading = state.isSaveLoading,
            onClick = {
                onAction(CreateAdditionGroupForMenu.Action.OnSaveClick)
            },
        )
    }
}
