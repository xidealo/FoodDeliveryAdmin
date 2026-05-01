package com.bunbeauty.shared.feature.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.FloatingButton
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_menu_list_create
import fooddeliveryadmin.shared.generated.resources.ic_check
import fooddeliveryadmin.shared.generated.resources.ic_edit
import fooddeliveryadmin.shared.generated.resources.ic_plus
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.title_categories_list
import fooddeliveryadmin.shared.generated.resources.title_categories_list_updated
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_edit_priority
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoryListRouteScreen(
    viewModel: CategoryListViewModel = koinViewModel(),
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToCreateCategoryScreen: () -> Unit,
    goToEditCategoryScreen: (String) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CategoryListState.Action ->
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
        onAction(CategoryListState.Action.Init)
    }

    CategoryListEffect(
        effects = effects,
        showInfoMessage = showInfoMessage,
        consumeEffects = consumeEffects,
        goBack = goBack,
        goToCreateCategoryScreen = goToCreateCategoryScreen,
        goToEditCategoryScreen = goToEditCategoryScreen,
    )

    CategoryListScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun CategoryListEffect(
    effects: List<CategoryListState.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToCreateCategoryScreen: () -> Unit,
    goToEditCategoryScreen: (String) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CategoryListState.Event.GoBackEvent -> {
                    goBack()
                }

                CategoryListState.Event.CreateCategoryEvent -> {
                    goToCreateCategoryScreen()
                }

                is CategoryListState.Event.OnCategoryClick -> {
                    goToEditCategoryScreen(effect.categoryUuid)
                }

                CategoryListState.Event.ShowUpdateCategoryListSuccess -> {
                    showInfoMessage(
                        getString(Res.string.title_categories_list_updated),
                        ButtonDefaults.MinHeight + 12.dp,
                    )
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun CategoryListScreen(
    state: CategoryListViewState,
    onAction: (CategoryListState.Action) -> Unit,
    goBack: () -> Unit,
) {
    AdminScaffold(
        backgroundColor = AdminTheme.colors.main.surface,
        title =
            when (state.state) {
                CategoryListViewState.State.Error -> null
                CategoryListViewState.State.Loading -> null
                is CategoryListViewState.State.Success -> stringResource(Res.string.title_categories_list)
                is CategoryListViewState.State.SuccessDragDrop -> stringResource(Res.string.title_edit_priority)
            },
        pullRefreshEnabled =
            when (state.state) {
                CategoryListViewState.State.Error -> false
                CategoryListViewState.State.Loading -> false
                is CategoryListViewState.State.Success -> true
                is CategoryListViewState.State.SuccessDragDrop -> false
            },
        refreshing = state.isRefreshing,
        onRefresh = {
            onAction(CategoryListState.Action.OnRefreshData)
        },
        backActionClick =
            {
                when (state.state) {
                    CategoryListViewState.State.Loading -> Unit
                    CategoryListViewState.State.Error -> Unit
                    is CategoryListViewState.State.Success -> onAction(CategoryListState.Action.OnBackClicked)
                    is CategoryListViewState.State.SuccessDragDrop -> onAction(CategoryListState.Action.OnCancelClicked)
                }
            },
        topActions =
            when (state.state) {
                CategoryListViewState.State.Error -> emptyList()
                CategoryListViewState.State.Loading -> emptyList()
                is CategoryListViewState.State.Success ->
                    listOf(
                        AdminTopBarAction(
                            iconId = Res.drawable.ic_edit,
                            color = AdminTheme.colors.main.primary,
                            onClick = {
                                onAction(CategoryListState.Action.OnPriorityEditClicked)
                            },
                        ),
                    )

                is CategoryListViewState.State.SuccessDragDrop ->
                    listOf(
                        AdminTopBarAction(
                            iconId = Res.drawable.ic_check,
                            color = AdminTheme.colors.main.primary,
                            onClick = {
                                onAction(
                                    CategoryListState.Action.OnSaveEditPriorityCategoryClick(
                                        updatedList = state.categoryList,
                                    ),
                                )
                            },
                        ),
                    )
            },
        actionButton = {
            when (state.state) {
                is CategoryListViewState.State.Success -> {
                    FloatingButton(
                        modifier = Modifier.bottomBarPadding(),
                        iconId = Res.drawable.ic_plus,
                        textStringId = Res.string.action_menu_list_create,
                        onClick = {
                            onAction(CategoryListState.Action.OnCreateClicked)
                        },
                    )
                }

                else -> Unit
            }
        },
        floatingActionButtonPosition =
            when (state.state) {
                is CategoryListViewState.State.Success -> Alignment.BottomEnd
                else -> Alignment.Center
            },
    ) {
        when (state.state) {
            CategoryListViewState.State.Loading -> {
                LoadingScreen()
            }

            CategoryListViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(CategoryListState.Action.Init)
                    },
                )
            }

            is CategoryListViewState.State.Success -> {
                CategoriesListSuccessScreen(state = state.state, onAction = onAction)
            }

            is CategoryListViewState.State.SuccessDragDrop -> {
                CategoriesListSuccessDragScreen(
                    state = state.state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun CategoriesListSuccessScreen(
    state: CategoryListViewState.State.Success,
    onAction: (CategoryListState.Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding =
            PaddingValues(
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace(),
            ),
    ) {
        items(
            items = state.categoryList,
            key = { category -> category.uuid },
        ) { category ->
            Column {
                CategoryItemView(
                    category = category,
                    onClick = {
                        onAction(
                            CategoryListState.Action.OnCategoryClick(
                                categoryUuid = category.uuid,
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
private fun CategoriesListSuccessDragScreen(
    state: CategoryListViewState.State.SuccessDragDrop,
    onAction: (CategoryListState.Action) -> Unit,
) {
    com.bunbeauty.shared.designsystem.compose.element.DragDropList(
        items = state.categoryList,
        itemKey = { it.uuid },
        onMove = { fromIndex, toIndex ->
            onAction(
                CategoryListState.Action.PutInItem(
                    fromIndex = fromIndex,
                    toIndex = toIndex,
                ),
            )
        },
        itemLabel = { it.name },
    )
}

@Composable
private fun CategoryItemView(
    modifier: Modifier = Modifier,
    category: CategoryListViewState.CategoriesViewItem,
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
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier =
                    Modifier
                        .weight(1f),
                text = category.name,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
            )
        }
    }
}
