package com.bunbeauty.shared.feature.additiongrouplist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.FloatingButton
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_addition_group_create
import fooddeliveryadmin.shared.generated.resources.hint_menu_list_search
import fooddeliveryadmin.shared.generated.resources.ic_plus
import fooddeliveryadmin.shared.generated.resources.ic_search
import fooddeliveryadmin.shared.generated.resources.ic_visible
import fooddeliveryadmin.shared.generated.resources.title_addition_group_list
import fooddeliveryadmin.shared.generated.resources.title_menu_list_position_hidden
import fooddeliveryadmin.shared.generated.resources.title_menu_list_position_visible
import fooddeliveryadmin.shared.generated.resources.title_menu_list_search_empty
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"

@Composable
fun AdditionGroupListRouteScreen(
    viewModel: AdditionGroupListViewModel = koinViewModel(),
    goBack: () -> Unit,
    goToCreateAdditionGroupScreen: () -> Unit,
    goToEditAdditionGroupScreen: (String) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: AdditionGroupList.Action ->
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
        onAction(AdditionGroupList.Action.Init)
    }

    AdditionEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
        goToEditAdditionGroupScreen = goToEditAdditionGroupScreen,
    )

    AdditionGroupListScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goToCreateAdditionGroupScreen = goToCreateAdditionGroupScreen,
        onBackClick = goBack,
    )
}

@Composable
private fun AdditionEffect(
    effects: List<AdditionGroupList.Event>,
    goBack: () -> Unit,
    goToEditAdditionGroupScreen: (String) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                AdditionGroupList.Event.Back -> {
                    goBack()
                }

                is AdditionGroupList.Event.OnAdditionGroupClick -> {
                    goToEditAdditionGroupScreen(effect.additionUuid)
                }
            }
        }
        consumeEffects()
    }
}

@Composable
fun AdditionGroupListScreen(
    state: AdditionGroupListViewState,
    onAction: (AdditionGroupList.Action) -> Unit,
    goToCreateAdditionGroupScreen: () -> Unit,
    onBackClick: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_addition_group_list),
        pullRefreshEnabled = true,
        refreshing = state.isRefreshing,
        onRefresh = {
            onAction(AdditionGroupList.Action.RefreshData)
        },
        backActionClick = onBackClick,
        topActions =
            if (!state.isLoading) {
                listOf(
                    AdminTopBarAction(
                        iconId = Res.drawable.ic_search,
                        color = AdminTheme.colors.main.primary,
                        onClick = {
                            onAction(AdditionGroupList.Action.OnSearchClicked)
                        },
                    ),
                )
            } else {
                emptyList()
            },
        actionButton = {
            FloatingButton(
                modifier = Modifier.bottomBarPadding(),
                iconId = Res.drawable.ic_plus,
                textStringId = Res.string.action_addition_group_create,
                onClick = goToCreateAdditionGroupScreen,
            )
        },
        floatingActionButtonPosition = Alignment.BottomEnd,
    ) {
        when {
            state.isLoading -> LoadingScreen()
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (state.isSearchEnabled) {
                        ListSearchField(
                            searchQuery = state.searchQuery,
                            onSearchQueryChange = { searchQuery ->
                                onAction(AdditionGroupList.Action.OnSearchQueryChange(searchQuery))
                            },
                        )
                    }
                    AdditionGroupListSuccess(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun AdditionGroupListSuccess(
    state: AdditionGroupListViewState,
    onAction: (AdditionGroupList.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.searchResultList == null -> {
            AdditionGroupListSeparatedContent(
                state = state,
                onAction = onAction,
                modifier = modifier,
            )
        }

        state.searchResultList.isEmpty() -> {
            ListSearchEmptyScreen(modifier = modifier)
        }

        else -> {
            AdditionGroupListSearchResultScreen(
                additionItems = state.searchResultList,
                onAction = onAction,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun AdditionGroupListSeparatedContent(
    state: AdditionGroupListViewState,
    onAction: (AdditionGroupList.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (state.visibleAdditionItems.isNotEmpty()) {
            item(
                key = TITLE_POSITION_VISIBLE_KEY,
            ) {
                Text(
                    text = stringResource(Res.string.title_menu_list_position_visible),
                    style = AdminTheme.typography.titleMedium.bold,
                )
            }
            items(
                items = state.visibleAdditionItems,
                key = { additionItem ->
                    additionItem.uuid
                },
            ) { visibleAddition ->
                AdditionGroupCard(
                    additionItem = visibleAddition,
                    onAction = onAction,
                )
            }
        }
        if (state.hiddenAdditionItems.isNotEmpty()) {
            item(
                key = TITLE_POSITION_HIDDEN_KEY,
            ) {
                Text(
                    text = stringResource(Res.string.title_menu_list_position_hidden),
                    style = AdminTheme.typography.titleMedium.bold,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            items(
                items = state.hiddenAdditionItems,
                key = { additionGroupItem ->
                    additionGroupItem.uuid
                },
            ) { hiddenAddition ->
                AdditionGroupCard(
                    additionItem = hiddenAddition,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun AdditionGroupListSearchResultScreen(
    additionItems: List<AdditionGroupListViewState.AdditionGroupItem>,
    onAction: (AdditionGroupList.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = additionItems,
            key = { additionItem ->
                additionItem.uuid
            },
        ) { additionItem ->
            AdditionGroupCard(
                additionItem = additionItem,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun ListSearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    AdminTextField(
        value = searchQuery,
        labelText = stringResource(Res.string.hint_menu_list_search),
        onValueChange = onSearchQueryChange,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun ListSearchEmptyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AdminTheme.dimensions.mediumSpace)
                    .align(Alignment.Center),
            text = stringResource(Res.string.title_menu_list_search_empty),
            style = AdminTheme.typography.titleMedium,
            color = AdminTheme.colors.main.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AdditionGroupCard(
    additionItem: AdditionGroupListViewState.AdditionGroupItem,
    onAction: (AdditionGroupList.Action) -> Unit,
) {
    AdminCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onAction(
                AdditionGroupList.Action.OnAdditionClick(
                    additionUuid = additionItem.uuid,
                ),
            )
        },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = additionItem.name,
                modifier = Modifier.weight(1f),
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
            )

            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    onAction(
                        AdditionGroupList.Action.OnVisibleClick(
                            isVisible = additionItem.isVisible,
                            uuid = additionItem.uuid,
                        ),
                    )
                },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_visible),
                    contentDescription = null,
                    tint = additionItem.iconColor,
                )
            }
        }
    }
}

@Preview
@Composable
fun AdditionListScreenPreview() {
    AdminTheme {
        AdditionGroupListScreen(
            state =
                AdditionGroupListViewState(
                    visibleAdditionItems =
                        persistentListOf(
                            AdditionGroupListViewState.AdditionGroupItem(
                                uuid = "1",
                                name = "additio1",
                                iconColor = AdminTheme.colors.main.primary,
                                isVisible = true,
                            ),
                        ),
                    hiddenAdditionItems =
                        persistentListOf(
                            AdditionGroupListViewState.AdditionGroupItem(
                                uuid = "2",
                                name = "additio2",
                                iconColor = AdminTheme.colors.main.onSecondary,
                                isVisible = false,
                            ),
                        ),
                    isRefreshing = false,
                    isLoading = false,
                    isSearchEnabled = false,
                    searchQuery = "",
                    searchResultList = null,
                ),
            onAction = {},
            goToCreateAdditionGroupScreen = {},
            onBackClick = {},
        )
    }
}
