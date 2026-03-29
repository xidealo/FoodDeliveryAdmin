package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.navigation.EditAdditionGroupForMenuProductScreenDestination
import com.bunbeauty.presentation.navigation.NavStateHandleParameters.SELECTED_ADDITION_GROUP_UUID
import com.bunbeauty.presentation.navigation.NavStateHandleParameters.SELECTED_ADDITION_UUID_LIST
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_edit_addition_group_for_menu_product_save
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_update_edit_addition_group_for_menu_product
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_edit_addition_group_for_menu_product
import fooddeliveryadmin.presentation.generated.resources.title_edit_addition_group_for_menu_product_addition
import fooddeliveryadmin.presentation.generated.resources.title_edit_addition_group_for_menu_product_group
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditAdditionGroupForMenuProductRouteScreen(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToSelectAdditionGroup: (String, String, String) -> Unit,
    goToSelectAdditionList: (String, String, String, List<String>?) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val route = backStackEntry.toRoute<EditAdditionGroupForMenuProductScreenDestination>()
    val viewModel: EditAdditionGroupForMenuProductViewModel = koinViewModel(
        parameters = {
            parametersOf(route.additionGroupUuid, route.menuProductUuid)
        },
    )
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: EditAdditionGroupForMenu.Action ->
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
        backStackEntry.savedStateHandle.getStateFlow(
            SELECTED_ADDITION_GROUP_UUID,
            viewState.editedAdditionGroupUuid,
        ).collect { selectedAdditionGroupUuid ->
            if (selectedAdditionGroupUuid != null) {
                onAction(
                    EditAdditionGroupForMenu.Action.SelectAdditionGroup(
                        additionGroupUuid = selectedAdditionGroupUuid,
                    ),
                )
                backStackEntry.savedStateHandle.remove<String>(SELECTED_ADDITION_GROUP_UUID)
            }
        }
    }

    LaunchedEffect(Unit) {
        backStackEntry.savedStateHandle.getStateFlow(
            SELECTED_ADDITION_UUID_LIST,
            viewState.editedAdditionListUuid,
        ).collect { selectedAdditionUuidList ->
            if (selectedAdditionUuidList != null) {
                onAction(
                    EditAdditionGroupForMenu.Action.SelectAdditionList(
                        additionListUuid = selectedAdditionUuidList,
                    ),
                )
                backStackEntry.savedStateHandle.remove<List<String>>(SELECTED_ADDITION_UUID_LIST)
            }
        }
    }

    EditAdditionGroupForMenuEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
        goToSelectAdditionGroup = goToSelectAdditionGroup,
        goToSelectAdditionList = goToSelectAdditionList,
    )

    EditAdditionGroupForMenuProductScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun EditAdditionGroupForMenuEffect(
    effects: List<EditAdditionGroupForMenu.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    goToSelectAdditionGroup: (String, String, String) -> Unit,
    goToSelectAdditionList: (String, String, String, List<String>?) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                EditAdditionGroupForMenu.Event.Back -> {
                    goBack()
                }

                EditAdditionGroupForMenu.Event.SaveAndBack -> {
                    showInfoMessage(
                        getString(
                            resource = Res.string.msg_update_edit_addition_group_for_menu_product,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                    goBack()
                }

                is EditAdditionGroupForMenu.Event.OnAdditionGroupClick -> {
                    goToSelectAdditionGroup(
                        effect.editedAdditionGroupUuid,
                        effect.menuProductUuid,
                        effect.mainEditedAdditionGroupUuid,
                    )
                }

                is EditAdditionGroupForMenu.Event.OnAdditionListClick -> {
                    goToSelectAdditionList(
                        effect.additionGroupUuid,
                        effect.menuProductUuid,
                        effect.additionGroupName,
                        effect.editedAdditionListUuid,
                    )
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun EditAdditionGroupForMenuProductScreen(
    state: EditAdditionGroupForMenuProductViewState,
    onAction: (EditAdditionGroupForMenu.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_edit_addition_group_for_menu_product),
        backActionClick = {
            onAction(EditAdditionGroupForMenu.Action.OnBackClick)
        },
        backgroundColor = AdminTheme.colors.main.surface,
    ) {
        when (state.state) {
            EditAdditionGroupForMenuProductViewState.State.Loading -> LoadingScreen()

            EditAdditionGroupForMenuProductViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(EditAdditionGroupForMenu.Action.OnBackClick)
                    },
                )
            }

            is EditAdditionGroupForMenuProductViewState.State.Success ->
                EditAdditionGroupForMenuProductSuccessScreen(
                    state = state.state,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun EditAdditionGroupForMenuProductSuccessScreen(
    state: EditAdditionGroupForMenuProductViewState.State.Success,
    onAction: (EditAdditionGroupForMenu.Action) -> Unit,
) {
    Column {
        NavigationTextCard(
            valueText = state.groupName.orEmpty(),
            onClick = {
                onAction(
                    EditAdditionGroupForMenu.Action.OnAdditionGroupClick(
                        uuid = state.additionGroupForMenuProductUuid,
                    ),
                )
            },
            elevated = false,
            labelText =
                stringResource(
                    resource = Res.string.title_edit_addition_group_for_menu_product_group,
                ),
            hasDivider = true,
        )

        NavigationTextCard(
            valueText = state.additionNameList.orEmpty(),
            onClick = {
                onAction(
                    EditAdditionGroupForMenu.Action.OnAdditionListClick(
                        uuid = state.additionGroupForMenuProductUuid,
                    ),
                )
            },
            elevated = false,
            labelText =
                stringResource(
                    resource = Res.string.title_edit_addition_group_for_menu_product_addition,
                ),
            hasDivider = true,
        )

        Spacer(modifier = Modifier.weight(1f))
        LoadingButton(
            modifier = Modifier.padding(16.dp),
            text = stringResource(Res.string.action_edit_addition_group_for_menu_product_save),
            isLoading = false,
            onClick = {
                onAction(EditAdditionGroupForMenu.Action.OnSaveClick)
            },
        )
    }
}
