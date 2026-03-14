package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.selectableitem.SelectableItem
import com.bunbeauty.presentation.designsystem.compose.element.selectableitem.SelectableItemView
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.bold
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.navigation.SelectAdditionGroupScreenDestination
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_select_addition_group_selected
import fooddeliveryadmin.presentation.generated.resources.title_addition_group_for_menu_product_empty
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_menu_list_position_hidden
import fooddeliveryadmin.presentation.generated.resources.title_select_addition_group
import fooddeliveryadmin.presentation.generated.resources.title_select_addition_group_position_visible
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"
private const val LIST_ANIMATION_DURATION = 500

@Composable
fun SelectAdditionGroupRouteScreen(
    viewModel: SelectAdditionGroupViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    onAdditionGroupSelected: (String, String) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val route = backStackEntry.toRoute<SelectAdditionGroupScreenDestination>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: SelectAdditionGroup.Action ->
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
            SelectAdditionGroup.Action.Init(
                selectedAdditionGroupUuid = route.additionGroupUuid,
                menuProductUuid = route.menuProductUuid,
                mainEditedAdditionGroupUuid = route.mainEditedAdditionGroupUuid,
            ),
        )
    }

    SelectAdditionGroupEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
        onAdditionGroupSelected = onAdditionGroupSelected,
    )

    SelectAdditionGroupScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun SelectAdditionGroupEffect(
    effects: List<SelectAdditionGroup.Event>,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    onAdditionGroupSelected: (String, String) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                SelectAdditionGroup.Event.Back -> {
                    goBack()
                }

                is SelectAdditionGroup.Event.SelectAdditionGroupClicked -> {
                    showInfoMessage(
                        getString(
                            resource = Res.string.msg_select_addition_group_selected,
                            effect.additionGroupName,
                        ),
                        2000,
                    )
                    onAdditionGroupSelected(
                        effect.additionGroupUuid,
                        effect.additionGroupName,
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun SelectAdditionGroupScreen(
    state: SelectAdditionGroupViewState,
    onAction: (SelectAdditionGroup.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_select_addition_group),
        backActionClick = {
            onAction(SelectAdditionGroup.Action.OnBackClick)
        },
        backgroundColor = AdminTheme.colors.main.surface,
    ) {
        when (state.state) {
            SelectAdditionGroupViewState.State.Loading -> LoadingScreen()

            SelectAdditionGroupViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(SelectAdditionGroup.Action.OnBackClick)
                    },
                )
            }

            is SelectAdditionGroupViewState.State.Success ->
                SelectAdditionGroupSuccessScreen(
                    state = state.state,
                    onAction = onAction,
                )

            is SelectAdditionGroupViewState.State.Empty -> {
                NoAvailableAdditionGroups()
            }
        }
    }
}

@Composable
private fun NoAvailableAdditionGroups() {
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
private fun SelectAdditionGroupSuccessScreen(
    state: SelectAdditionGroupViewState.State.Success,
    onAction: (SelectAdditionGroup.Action) -> Unit,
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
                        ).animateItem()
                        .animateContentSize(
                            animationSpec = tween(LIST_ANIMATION_DURATION),
                        ),
                text = stringResource(Res.string.title_select_addition_group_position_visible),
                style = AdminTheme.typography.titleMedium.bold,
            )
        }

        items(
            items = state.visibleSelectableAdditionGroupList,
            key = { selectableAdditionGroup ->
                selectableAdditionGroup.uuid
            },
        ) { selectableCategory ->
            SelectableItemView(
                modifier =
                    Modifier
                        .animateItem(),
                selectableItem =
                    SelectableItem(
                        uuid = selectableCategory.uuid,
                        title = selectableCategory.name,
                        isSelected = selectableCategory.isSelected,
                    ),
                hasDivider = true,
                onClick = {
                    onAction(
                        SelectAdditionGroup.Action.SelectAdditionGroupClick(
                            uuid = selectableCategory.uuid,
                            name = selectableCategory.name,
                        ),
                    )
                },
                elevated = false,
                isClickable = true,
            )
        }

        if (state.hiddenSelectableAdditionGroupList.isNotEmpty()) {
            item(
                key = TITLE_POSITION_HIDDEN_KEY,
            ) {
                Text(
                    modifier =
                        Modifier
                            .padding(
                                all = 16.dp,
                            ).animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION),
                            ),
                    text = stringResource(Res.string.title_menu_list_position_hidden),
                    style = AdminTheme.typography.titleMedium.bold,
                )
            }
            items(
                items = state.hiddenSelectableAdditionGroupList,
                key = { hiddenAdditionGroup ->
                    hiddenAdditionGroup.uuid
                },
            ) { hiddenAdditionGroup ->
                SelectableItemView(
                    modifier =
                        Modifier
                            .animateItem(),
                    selectableItem =
                        SelectableItem(
                            uuid = hiddenAdditionGroup.uuid,
                            title = hiddenAdditionGroup.name,
                            isSelected = hiddenAdditionGroup.isSelected,
                        ),
                    hasDivider = true,
                    onClick = {
                        onAction(
                            SelectAdditionGroup.Action.SelectAdditionGroupClick(
                                uuid = hiddenAdditionGroup.uuid,
                                name = hiddenAdditionGroup.name,
                            ),
                        )
                    },
                    elevated = false,
                    isClickable = true,
                )
            }
        }
    }
}
