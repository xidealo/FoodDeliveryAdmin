package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition

import androidx.compose.ui.unit.Dp
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.DragDropList
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.card.AdminCard
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.bold
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.navigation.SelectAdditionListScreenDestination
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_order_details_save
import fooddeliveryadmin.presentation.generated.resources.action_select_addition_list_title_additions
import fooddeliveryadmin.presentation.generated.resources.action_select_addition_list_title_group
import fooddeliveryadmin.presentation.generated.resources.action_select_addition_list_title_selected
import fooddeliveryadmin.presentation.generated.resources.ic_check
import fooddeliveryadmin.presentation.generated.resources.ic_edit
import fooddeliveryadmin.presentation.generated.resources.ic_minus
import fooddeliveryadmin.presentation.generated.resources.ic_plus
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_update_select_addition_list_added
import fooddeliveryadmin.presentation.generated.resources.msg_update_select_addition_list_priority_list
import fooddeliveryadmin.presentation.generated.resources.msg_update_select_addition_list_removed
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_edit_priority
import fooddeliveryadmin.presentation.generated.resources.title_select_addition_list
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_ADDITIONS_KEY = "title_position_additions"
private const val LIST_ANIMATION_DURATION = 500

@Composable
fun SelectAdditionListRouteScreen(
    viewModel: SelectAdditionListViewModel = koinViewModel(),
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    onAdditionListSelected: (List<String>) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val route = backStackEntry.toRoute<SelectAdditionListScreenDestination>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: SelectAdditionList.Action ->
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
        onAction(
            SelectAdditionList.Action.Init(
                menuProductUuid = route.menuProductUuid,
                additionGroupUuid = route.additionGroupUuid,
                additionGroupName = route.additionGroupName,
                editedAdditionListUuid = route.editedAdditionListUuid?.ifEmpty { null },
            ),
        )
    }

    SelectAdditionListEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = goBack,
        onAdditionListSelected = onAdditionListSelected,
    )

    SelectAdditionScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun SelectAdditionListEffect(
    effects: List<SelectAdditionList.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    onAdditionListSelected: (List<String>) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                SelectAdditionList.Event.Back -> {
                    goBack()
                }

                is SelectAdditionList.Event.SelectAdditionListBack -> {
                    showInfoMessage(
                        getString(
                            resource = Res.string.action_select_addition_list_title_selected,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                    onAdditionListSelected(effect.additionUuidList)
                    goBack()
                }

                SelectAdditionList.Event.ShowUpdateSelectAdditionListSuccess -> {
                    showInfoMessage(
                        getString(
                            resource = Res.string.msg_update_select_addition_list_priority_list,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                }

                is SelectAdditionList.Event.AddedAddition -> {
                    showInfoMessage(
                        getString(
                            resource = Res.string.msg_update_select_addition_list_added,
                            effect.name,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                }

                is SelectAdditionList.Event.RemovedAddition -> {
                    showErrorMessage(
                        getString(
                            resource = Res.string.msg_update_select_addition_list_removed,
                            effect.name,
                        ),
                    )
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun SelectAdditionScreen(
    state: SelectAdditionListViewState,
    onAction: (SelectAdditionList.Action) -> Unit,
) {
    val title =
        when (state.state) {
            SelectAdditionListViewState.State.Loading,
            SelectAdditionListViewState.State.Error,
            -> null

            is SelectAdditionListViewState.State.Success -> stringResource(Res.string.title_select_addition_list)
            is SelectAdditionListViewState.State.SuccessDragDrop -> stringResource(Res.string.title_edit_priority)
        }

    val topActions: List<AdminTopBarAction> =
        when (state.state) {
            SelectAdditionListViewState.State.Loading, SelectAdditionListViewState.State.Error -> emptyList()
            is SelectAdditionListViewState.State.Success ->
                if (state.state.emptySelectedList) {
                    emptyList()
                } else {
                    listOf(
                        AdminTopBarAction(
                            iconId = Res.drawable.ic_edit,
                            color = AdminTheme.colors.main.primary,
                            onClick = {
                                onAction(SelectAdditionList.Action.OnPriorityEditClicked)
                            },
                        ),
                    )
                }

            is SelectAdditionListViewState.State.SuccessDragDrop ->
                listOf(
                    AdminTopBarAction(
                        iconId = Res.drawable.ic_check,
                        color = AdminTheme.colors.main.primary,
                        onClick = {
                            onAction(SelectAdditionList.Action.OnSaveEditPriorityClick)
                        },
                    ),
                )
        }

    AdminScaffold(
        title = title,
        backActionClick = {
            when (state.state) {
                SelectAdditionListViewState.State.Loading, SelectAdditionListViewState.State.Error -> Unit
                is SelectAdditionListViewState.State.Success -> onAction(SelectAdditionList.Action.OnBackClick)
                is SelectAdditionListViewState.State.SuccessDragDrop -> onAction(SelectAdditionList.Action.OnCancelClicked)
            }
        },
        backgroundColor = AdminTheme.colors.main.surface,
        actionButton = {
            if (state.state is SelectAdditionListViewState.State.Success) {
                LoadingButton(
                    text = stringResource(Res.string.action_order_details_save),
                    isLoading = false,
                    onClick = {
                        onAction(SelectAdditionList.Action.SelectAdditionListClick)
                    },
                    modifier =
                        Modifier
                            .padding(horizontal = AdminTheme.dimensions.mediumSpace),
                )
            }
        },
        topActions = topActions,
    ) {
        when (state.state) {
            SelectAdditionListViewState.State.Loading -> LoadingScreen()

            SelectAdditionListViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(SelectAdditionList.Action.OnBackClick)
                    },
                )
            }

            is SelectAdditionListViewState.State.Success ->
                SelectAdditionSuccessScreen(
                    state = state.state,
                    onAction = onAction,
                )

            is SelectAdditionListViewState.State.SuccessDragDrop ->
                SelectAdditionSuccessDragScreen(
                    state = state.state,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun SelectAdditionSuccessScreen(
    state: SelectAdditionListViewState.State.Success,
    onAction: (SelectAdditionList.Action) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize(),
        contentPadding =
            PaddingValues(
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace,
            ),
    ) {
        item(
            key = TITLE_POSITION_VISIBLE_KEY,
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(
                            start = 16.dp,
                            bottom = 16.dp,
                        ).fillMaxWidth()
                        .animateItem()
                        .animateContentSize(
                            animationSpec = tween(LIST_ANIMATION_DURATION),
                        ),
                text =
                    buildAnnotatedString {
                        append(
                            stringResource(
                                Res.string.action_select_addition_list_title_group,
                                state.groupName,
                            ),
                        )
                    },
                style = AdminTheme.typography.titleMedium.bold,
            )
        }

        items(
            items = state.selectedAdditionList,
            key = { additions -> additions.uuid },
        ) { additionItem ->
            SelectAdditionCard(
                additionItem = additionItem,
                icon = Res.drawable.ic_minus,
                iconColor = AdminTheme.colors.main.onSurfaceVariant,
                onClick = {
                    onAction(
                        SelectAdditionList.Action.RemoveAdditionClick(
                            uuid = additionItem.uuid,
                        ),
                    )
                },
            )
        }

        item(
            key = TITLE_POSITION_ADDITIONS_KEY,
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(
                            all = 16.dp,
                        ).fillMaxWidth()
                        .animateItem()
                        .animateContentSize(
                            animationSpec = tween(LIST_ANIMATION_DURATION),
                        ),
                text = stringResource(Res.string.action_select_addition_list_title_additions),
                style = AdminTheme.typography.titleMedium.bold,
            )
        }

        items(
            items = state.notSelectedAdditionList,
            key = { additions -> additions.uuid },
        ) { additionItem ->
            SelectAdditionCard(
                additionItem = additionItem,
                icon = Res.drawable.ic_plus,
                iconColor = AdminTheme.colors.main.primary,
                onClick = {
                    onAction(
                        SelectAdditionList.Action.SelectAdditionClick(
                            uuid = additionItem.uuid,
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun SelectAdditionCard(
    additionItem: SelectAdditionListViewState.AdditionItem,
    icon: DrawableResource,
    iconColor: Color,
    onClick: () -> Unit,
) {
    AdminCard(
        modifier = Modifier.fillMaxWidth(),
        clickable = false,
        elevated = false,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = additionItem.name,
                    modifier = Modifier.weight(1f),
                    style = AdminTheme.typography.bodyLarge,
                )

                IconButton(
                    modifier =
                        Modifier
                            .padding(vertical = 8.dp),
                    onClick = onClick,
                ) {
                    Icon(
                        painter = painterResource(resource = icon),
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            AdminHorizontalDivider()
        }
    }
}

@Composable
private fun SelectAdditionSuccessDragScreen(
    state: SelectAdditionListViewState.State.SuccessDragDrop,
    onAction: (SelectAdditionList.Action) -> Unit,
) {
    DragDropList(
        title = state.groupName,
        items = state.selectedAdditionList,
        itemKey = { it.uuid },
        onMove = { fromIndex, toIndex ->
            onAction(
                SelectAdditionList.Action.MoveSelectedItem(
                    fromIndex = fromIndex,
                    toIndex = toIndex,
                ),
            )
        },
        itemLabel = { it.name },
    )
}
