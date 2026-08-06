package com.bunbeauty.shared.feature.statisticuser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.shared.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_search
import fooddeliveryadmin.shared.generated.resources.ic_close
import fooddeliveryadmin.shared.generated.resources.ic_search
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_total
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_menu_list_search_empty
import fooddeliveryadmin.shared.generated.resources.title_statistic_user
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun StatisticUserRouteScreen(
    viewModel: StatisticUserViewModel = koinViewModel(),
    goToUserDetails: (String) -> Unit,
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { action: StatisticUser.Action ->
                viewModel.onAction(action)
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
        onAction(StatisticUser.Action.Init)
    }

    StatisticUserEffect(
        effects = effects,
        goToUserDetails = goToUserDetails,
        goBack = goBack,
        consumeEffects = consumeEffects,
    )

    StatisticUserScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun StatisticUserEffect(
    effects: List<StatisticUser.Event>,
    goToUserDetails: (String) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                is StatisticUser.Event.OpenUserDetails -> goToUserDetails(effect.userUuid)
                StatisticUser.Event.GoBack -> goBack()
            }
        }
        consumeEffects()
    }
}

@Composable
private fun StatisticUserScreen(
    state: StatisticUserViewState,
    onAction: (StatisticUser.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_statistic_user),
        backActionClick = {
            onAction(StatisticUser.Action.BackClick)
        },
        backgroundColor = AdminTheme.colors.main.surface,
        topActions =
            if (state.state is StatisticUserViewState.State.Success) {
                listOf(
                    AdminTopBarAction(
                        iconId =
                            if (state.state.isSearchEnabled) {
                                Res.drawable.ic_close
                            } else {
                                Res.drawable.ic_search
                            },
                        color = AdminTheme.colors.main.primary,
                        onClick = {
                            onAction(StatisticUser.Action.SearchClick)
                        },
                    ),
                )
            } else {
                emptyList()
            },
    ) {
        when (val currentState = state.state) {
            StatisticUserViewState.State.Loading -> {
                LoadingScreen()
            }

            StatisticUserViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(StatisticUser.Action.Init)
                    },
                )
            }

            is StatisticUserViewState.State.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (currentState.isSearchEnabled) {
                        StatisticUserSearchField(
                            searchQuery = currentState.searchQuery,
                            onSearchQueryChange = { searchQuery ->
                                onAction(StatisticUser.Action.SearchQueryChange(searchQuery))
                            },
                        )
                    }
                    StatisticUserSuccessContent(
                        modifier = Modifier.weight(1f),
                        state = currentState,
                        onAction = onAction,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticUserSuccessContent(
    state: StatisticUserViewState.State.Success,
    onAction: (StatisticUser.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.searchResultList == null -> {
            StatisticUserList(
                users = state.users,
                canLoadMore = state.canLoadMore,
                isPageLoading = state.isPageLoading,
                onUserClick = { uuid ->
                    onAction(StatisticUser.Action.UserClick(uuid))
                },
                onLoadMore = {
                    onAction(StatisticUser.Action.LoadMore)
                },
                modifier = modifier,
                userCount = state.totalUsers,
                isSearch = state.isSearchEnabled,
            )
        }

        state.searchResultList.isEmpty() -> {
            StatisticUserSearchEmptyScreen(modifier = modifier)
        }

        else -> {
            StatisticUserList(
                users = state.searchResultList,
                canLoadMore = state.searchCanLoadMore,
                isPageLoading = state.isSearchLoading,
                onUserClick = { uuid ->
                    onAction(StatisticUser.Action.UserClick(uuid))
                },
                onLoadMore = {
                    onAction(StatisticUser.Action.LoadMore)
                },
                modifier = modifier,
                userCount = state.totalUsers,
                isSearch = state.isSearchEnabled,
            )
        }
    }
}

@Composable
private fun StatisticUserList(
    users: List<StatisticUserViewState.UserItem>,
    canLoadMore: Boolean,
    isPageLoading: Boolean,
    userCount: Int,
    isSearch: Boolean,
    onUserClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember(users.size) {
        derivedStateOf {
            val lastVisibleIndex =
                listState.layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index ?: 0
            lastVisibleIndex >= users.size - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(shouldLoadMore, users.size, isPageLoading) {
        if (canLoadMore && shouldLoadMore && !isPageLoading) {
            onLoadMore()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        contentPadding =
            PaddingValues(
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace(),
            ),
    ) {
        if (!isSearch) {
            item {
                StatisticUserCommonCount(
                    stringResource(Res.string.msg_statistic_user_total),
                    userCount.toString(),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp),
                )
            }
        }
        items(
            items = users,
            key = { user -> user.uuid },
        ) { user ->
            StatisticUserItemView(
                user = user,
                onClick = {
                    onUserClick(user.uuid)
                },
            )
            AdminHorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        if (isPageLoading) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = AdminTheme.colors.main.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticUserItemView(
    user: StatisticUserViewState.UserItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = noCornerCardShape,
        elevated = false,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = user.phoneNumber,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
            )
        }
    }
}

@Composable
private fun StatisticUserSearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    AdminTextField(
        value = searchQuery,
        labelText = stringResource(Res.string.hint_statistic_user_search),
        onValueChange = onSearchQueryChange,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun StatisticUserSearchEmptyScreen(modifier: Modifier = Modifier) {
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
private fun StatisticUserCommonCount(
    hint: String,
    info: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = hint,
            style = AdminTheme.typography.labelSmall.medium,
            color = AdminTheme.colors.main.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = info,
            style = AdminTheme.typography.bodyMedium,
            color = AdminTheme.colors.main.onSurface,
        )
    }
}

@Preview
@Composable
private fun StatisticUserScreenPreview() {
    AdminTheme {
        StatisticUserScreen(
            state =
                StatisticUserViewState(
                    state =
                        StatisticUserViewState.State.Success(
                            users =
                                persistentListOf(
                                    StatisticUserViewState.UserItem(
                                        uuid = "1",
                                        phoneNumber = "+7 996 922 41 86",
                                    ),
                                    StatisticUserViewState.UserItem(
                                        uuid = "2",
                                        phoneNumber = "+7 996 922 41 87",
                                    ),
                                ),
                            totalUsers = 2,
                            isSearchEnabled = false,
                            searchQuery = "",
                            searchResultList = null,
                            canLoadMore = false,
                            isPageLoading = false,
                            searchCanLoadMore = false,
                            isSearchLoading = false,
                        ),
                ),
            onAction = {},
        )
    }
}
