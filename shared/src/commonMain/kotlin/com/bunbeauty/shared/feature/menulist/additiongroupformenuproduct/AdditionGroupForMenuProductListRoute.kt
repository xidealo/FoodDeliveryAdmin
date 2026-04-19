package com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.DragDropList
import com.bunbeauty.shared.designsystem.compose.element.button.FloatingButton
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.navigation.AdditionGroupForMenuProductListScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_addition_group_for_menu_product_addition_add
import fooddeliveryadmin.shared.generated.resources.ic_check
import fooddeliveryadmin.shared.generated.resources.ic_edit
import fooddeliveryadmin.shared.generated.resources.ic_plus
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.msg_update_addition_group_for_menu_product_priority_list
import fooddeliveryadmin.shared.generated.resources.title_addition_group_for_menu_product
import fooddeliveryadmin.shared.generated.resources.title_addition_group_for_menu_product_empty
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_edit_priority
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdditionGroupForMenuProductListRouteScreen(
    viewModel: AdditionGroupForMenuProductListViewModel = koinViewModel(),
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToCreateAdditionGroup: (String) -> Unit,
    goToEditAdditionGroup: (String, String) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val menuProductUuid =
        backStackEntry
            .toRoute<AdditionGroupForMenuProductListScreenDestination>()
            .menuProductUuid

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: AdditionGroupForMenuProductList.Action ->
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
        onAction(AdditionGroupForMenuProductList.Action.Init(menuProductUuid = menuProductUuid))
    }

    AdditionGroupForMenuProductEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
        goToCreateAdditionGroup = { goToCreateAdditionGroup(menuProductUuid) },
        goToEditAdditionGroup = { uuid ->
            goToEditAdditionGroup(menuProductUuid, uuid)
        },
    )

    AdditionGroupForMenuProductScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        menuProductUuid = menuProductUuid,
    )
}

@Composable
private fun AdditionGroupForMenuProductEffect(
    effects: List<AdditionGroupForMenuProductList.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    goToCreateAdditionGroup: () -> Unit,
    goToEditAdditionGroup: (String) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                AdditionGroupForMenuProductList.Event.Back -> {
                    goBack()
                }

                AdditionGroupForMenuProductList.Event.OnCreateClicked -> {
                    goToCreateAdditionGroup()
                }

                is AdditionGroupForMenuProductList.Event.OnAdditionGroupClicked -> {
                    goToEditAdditionGroup(effect.additionGroupUuid)
                }

                AdditionGroupForMenuProductList.Event.ShowUpdateAdditionGroupListSuccess -> {
                    showInfoMessage(
                        getString(
                            resource = Res.string.msg_update_addition_group_for_menu_product_priority_list,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun AdditionGroupForMenuProductScreen(
    state: AdditionGroupForMenuProductListViewState,
    menuProductUuid: String,
    onAction: (AdditionGroupForMenuProductList.Action) -> Unit,
) {
    AdminScaffold(
        title =
            when (state.state) {
                is AdditionGroupForMenuProductListViewState.State.Success ->
                    stringResource(
                        resource = Res.string.title_addition_group_for_menu_product,
                    )

                is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop ->
                    stringResource(
                        resource = Res.string.title_edit_priority,
                    )

                AdditionGroupForMenuProductListViewState.State.Error,
                AdditionGroupForMenuProductListViewState.State.Loading,
                -> null
            },
        pullRefreshEnabled = true,
        refreshing = state.isRefreshing,
        onRefresh = {
            onAction(
                AdditionGroupForMenuProductList.Action.RefreshData(
                    menuProductUuid = menuProductUuid,
                ),
            )
        },
        backActionClick = {
            when (state.state) {
                AdditionGroupForMenuProductListViewState.State.Error -> Unit
                AdditionGroupForMenuProductListViewState.State.Loading -> Unit
                is AdditionGroupForMenuProductListViewState.State.Success ->
                    onAction(
                        AdditionGroupForMenuProductList.Action.OnBackClick,
                    )

                is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop ->
                    onAction(
                        AdditionGroupForMenuProductList.Action.OnCancelClicked,
                    )
            }
        },
        backgroundColor = AdminTheme.colors.main.surface,
        topActions =
            when (state.state) {
                is AdditionGroupForMenuProductListViewState.State.Success ->
                    if (state.canBeChangedPlace) {
                        emptyList()
                    } else {
                        listOf(
                            AdminTopBarAction(
                                iconId = Res.drawable.ic_edit,
                                color = AdminTheme.colors.main.primary,
                                onClick = {
                                    onAction(AdditionGroupForMenuProductList.Action.OnPriorityEditClicked)
                                },
                            ),
                        )
                    }

                is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop ->
                    listOf(
                        AdminTopBarAction(
                            iconId = Res.drawable.ic_check,
                            color = AdminTheme.colors.main.primary,
                            onClick = {
                                onAction(
                                    AdditionGroupForMenuProductList.Action.OnSaveEditPriorityClick(
                                        updateAdditionGroupForMenuProductList = state.state.additionGroupWithAdditionsList.toList(),
                                    ),
                                )
                            },
                        ),
                    )

                AdditionGroupForMenuProductListViewState.State.Error,
                AdditionGroupForMenuProductListViewState.State.Loading,
                -> emptyList()
            },
        actionButton = {
            when (state.state) {
                is AdditionGroupForMenuProductListViewState.State.Success -> {
                    FloatingButton(
                        modifier = Modifier.bottomBarPadding(),
                        iconId = Res.drawable.ic_plus,
                        textStringId = Res.string.action_addition_group_for_menu_product_addition_add,
                        onClick = {
                            onAction(AdditionGroupForMenuProductList.Action.OnCreateClick)
                        },
                    )
                }

                AdditionGroupForMenuProductListViewState.State.Error,
                AdditionGroupForMenuProductListViewState.State.Loading,
                is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop,
                -> Unit
            }
        },
        floatingActionButtonPosition = Alignment.BottomEnd,
    ) {
        when (state.state) {
            is AdditionGroupForMenuProductListViewState.State.Loading -> LoadingScreen()
            is AdditionGroupForMenuProductListViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(
                            AdditionGroupForMenuProductList.Action.Init(
                                menuProductUuid = menuProductUuid,
                            ),
                        )
                    },
                )
            }

            is AdditionGroupForMenuProductListViewState.State.Success -> {
                if (state.state.additionGroupWithAdditionsList.isEmpty()) {
                    EmptyAdditionGroupForMenuProductScreen()
                } else {
                    AdditionGroupForMenuProductScreenSuccess(
                        state = state.state,
                        onAction = onAction,
                    )
                }
            }

            is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop -> {
                AdditionGroupForMenuProductScreenSuccessDragDrop(
                    state = state.state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun AdditionGroupForMenuProductScreenSuccess(
    state: AdditionGroupForMenuProductListViewState.State.Success,
    onAction: (AdditionGroupForMenuProductList.Action) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        items(
            items = state.additionGroupWithAdditionsList,
            key = { additionGroup -> additionGroup.uuid },
        ) { additionGroup ->
            Column {
                AdditionGroupItemView(
                    additionGroup = additionGroup,
                    onClick = {
                        onAction(
                            AdditionGroupForMenuProductList.Action.OnAdditionGroupClick(
                                uuid = additionGroup.uuid,
                            ),
                        )
                    },
                    isClickable = true,
                )
                AdminHorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyAdditionGroupForMenuProductScreen() {
    Box(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AdminTheme.dimensions.mediumSpace)
                    .align(Alignment.Center),
            text = stringResource(Res.string.title_addition_group_for_menu_product_empty),
            style = AdminTheme.typography.titleMedium,
            color = AdminTheme.colors.main.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AdditionGroupItemView(
    modifier: Modifier = Modifier,
    additionGroup: com.bunbeauty.domain.model.additiongroup.AdditionGroupForMenuProduct,
    onClick: () -> Unit,
    isClickable: Boolean,
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        clickable = isClickable,
        shape = noCornerCardShape,
        elevated = false,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ).fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = additionGroup.name,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
            )
            additionGroup.additionNameList?.let { additionNameList ->
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                    text = additionNameList,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun AdditionGroupForMenuProductScreenSuccessDragDrop(
    state: AdditionGroupForMenuProductListViewState.State.SuccessDragDrop,
    onAction: (AdditionGroupForMenuProductList.Action) -> Unit,
) {
    DragDropList(
        items = state.additionGroupWithAdditionsList.toList(),
        itemKey = { it.uuid },
        onMove = { fromIndex, toIndex ->
            onAction(
                AdditionGroupForMenuProductList.Action.MoveSelectedItem(
                    fromIndex = fromIndex,
                    toIndex = toIndex,
                ),
            )
        },
        itemLabel = { it.name },
    )
}
