package com.bunbeauty.shared.feature.additionlist

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.FloatingButton
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.shared.designsystem.compose.element.image.ImageData
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_addition_list_create
import fooddeliveryadmin.shared.generated.resources.default_product
import fooddeliveryadmin.shared.generated.resources.description_product
import fooddeliveryadmin.shared.generated.resources.error_common_loading_failed
import fooddeliveryadmin.shared.generated.resources.ic_plus
import fooddeliveryadmin.shared.generated.resources.ic_visible
import fooddeliveryadmin.shared.generated.resources.title_addition_list
import fooddeliveryadmin.shared.generated.resources.title_menu_list_position_hidden
import fooddeliveryadmin.shared.generated.resources.title_menu_list_position_visible
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"
private const val LIST_ANIMATION_DURATION = 500

@Composable
fun AdditionListRouteScreen(
    viewModel: AdditionListViewModel = koinViewModel(),
    goBack: () -> Unit,
    goToCreateAdditionScreen: () -> Unit,
    goToEditAdditionScreen: (String) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: AdditionList.Action ->
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
        onAction(AdditionList.Action.Init)
    }

    AdditionEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
        goToEditAdditionScreen = goToEditAdditionScreen,
    )

    AdditionListScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goToCreateAdditionScreen = goToCreateAdditionScreen,
        onBackClick = goBack,
    )
}

@Composable
private fun AdditionEffect(
    effects: List<AdditionList.Event>,
    goBack: () -> Unit,
    goToEditAdditionScreen: (String) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                AdditionList.Event.Back -> {
                    goBack()
                }

                is AdditionList.Event.OnAdditionClick -> {
                    goToEditAdditionScreen(effect.additionUuid)
                }
            }
        }
        consumeEffects()
    }
}

@Composable
fun AdditionListScreen(
    state: AdditionListViewState,
    onAction: (AdditionList.Action) -> Unit,
    goToCreateAdditionScreen: () -> Unit,
    onBackClick: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_addition_list),
        pullRefreshEnabled = true,
        refreshing = state.isRefreshing,
        onRefresh = {
            onAction(AdditionList.Action.RefreshData)
        },
        backActionClick = onBackClick,
        actionButton = {
            if (!state.isLoading && !state.hasError) {
                FloatingButton(
                    modifier = Modifier.bottomBarPadding(),
                    iconId = Res.drawable.ic_plus,
                    textStringId = Res.string.action_addition_list_create,
                    onClick = goToCreateAdditionScreen,
                )
            }
        },
        floatingActionButtonPosition = Alignment.BottomEnd,
    ) {
        when {
            state.hasError -> {
                ErrorScreen(
                    mainTextId = Res.string.error_common_loading_failed,
                    isLoading = state.isLoading,
                    onClick = {
                        onAction(AdditionList.Action.Init)
                    },
                )
            }

            state.isLoading -> LoadingScreen()

            else -> AdditionListSuccessScreen(state = state, onAction = onAction)
        }
    }
}

@Composable
private fun AdditionListSuccessScreen(
    state: AdditionListViewState,
    onAction: (AdditionList.Action) -> Unit,
) {
    LazyColumn(
        contentPadding =
            PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace(),
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (state.visibleAdditionItems.isNotEmpty()) {
            item(
                key = TITLE_POSITION_VISIBLE_KEY,
            ) {
                Text(
                    modifier =
                        Modifier
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION),
                            ),
                    text = stringResource(Res.string.title_menu_list_position_visible),
                    style = AdminTheme.typography.titleMedium.bold,
                )
            }
            items(
                items = state.visibleAdditionItems,
                key = { additionItem ->
                    additionItem.key
                },
            ) { visibleAddition ->
                when (visibleAddition) {
                    is AdditionListViewState.AdditionFeedViewItem.AdditionItem ->
                        AdditionCard(
                            modifier =
                                Modifier
                                    .animateItem()
                                    .animateContentSize(
                                        animationSpec = tween(LIST_ANIMATION_DURATION),
                                    ),
                            additionItem = visibleAddition.addition,
                            onAction = onAction,
                        )

                    is AdditionListViewState.AdditionFeedViewItem.Title ->
                        Text(
                            modifier =
                                Modifier
                                    .animateItem()
                                    .animateContentSize(
                                        animationSpec = tween(LIST_ANIMATION_DURATION),
                                    ),
                            text = visibleAddition.title,
                            style = AdminTheme.typography.titleSmall.bold,
                        )
                }
            }
        }

        if (state.hiddenAdditionItems.isNotEmpty()) {
            item(
                key = TITLE_POSITION_HIDDEN_KEY,
            ) {
                Text(
                    modifier =
                        Modifier
                            .padding(top = 8.dp)
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION),
                            ),
                    text = stringResource(Res.string.title_menu_list_position_hidden),
                    style = AdminTheme.typography.titleMedium.bold,
                )
            }
            items(
                items = state.hiddenAdditionItems,
                key = { additionGroupItem ->
                    additionGroupItem.key
                },
            ) { hiddenAddition ->
                when (hiddenAddition) {
                    is AdditionListViewState.AdditionFeedViewItem.AdditionItem ->
                        AdditionCard(
                            modifier =
                                Modifier
                                    .animateItem()
                                    .animateContentSize(
                                        animationSpec = tween(LIST_ANIMATION_DURATION),
                                    ),
                            additionItem = hiddenAddition.addition,
                            onAction = onAction,
                        )

                    is AdditionListViewState.AdditionFeedViewItem.Title ->
                        Text(
                            modifier =
                                Modifier
                                    .animateItem()
                                    .animateContentSize(
                                        animationSpec = tween(LIST_ANIMATION_DURATION),
                                    ),
                            text = hiddenAddition.title,
                            style = AdminTheme.typography.titleSmall.bold,
                        )
                }
            }
        }
    }
}

@Composable
private fun AdditionCard(
    modifier: Modifier,
    additionItem: AdditionListViewState.AdditionViewItem,
    onAction: (AdditionList.Action) -> Unit,
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onAction(AdditionList.Action.OnAdditionClick(additionUuid = additionItem.uuid))
        },
        elevated = false,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AdminAsyncImage(
                imageData = ImageData.HttpUrl(additionItem.photoLink),
                contentDescription = Res.string.description_product,
                placeholder = Res.drawable.default_product,
                modifier =
                    Modifier
                        .width(AdminTheme.dimensions.productImageSmallWidth)
                        .height(AdminTheme.dimensions.productImageSmallHeight),
            )

            Text(
                text = additionItem.name,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(top = AdminTheme.dimensions.smallSpace)
                        .padding(horizontal = AdminTheme.dimensions.smallSpace),
            )

            IconButton(
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = AdminTheme.dimensions.smallSpace),
                onClick = {
                    onAction(
                        AdditionList.Action.OnVisibleClick(
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

@Preview()
@Composable
fun AdditionListScreenPreview() {
    AdminTheme {
        AdditionListScreen(
            state =
                AdditionListViewState(
                    visibleAdditionItems =
                        persistentListOf(
                            AdditionListViewState
                                .AdditionFeedViewItem
                                .Title(key = "1", "Заголовок"),
                            AdditionListViewState
                                .AdditionFeedViewItem
                                .AdditionItem(
                                    key = "2",
                                    AdditionListViewState.AdditionViewItem(
                                        uuid = "1",
                                        name = "additio1",
                                        photoLink = "",
                                        iconColor = AdminTheme.colors.main.primary,
                                        isVisible = true,
                                    ),
                                ),
                        ),
                    hiddenAdditionItems =
                        persistentListOf(
                            AdditionListViewState
                                .AdditionFeedViewItem
                                .Title(
                                    key = "3",
                                    title = "Заголовок",
                                ),
                            AdditionListViewState
                                .AdditionFeedViewItem
                                .AdditionItem(
                                    key = "4",
                                    AdditionListViewState.AdditionViewItem(
                                        uuid = "2",
                                        name = "additio2",
                                        photoLink = "",
                                        iconColor = AdminTheme.colors.main.primary,
                                        isVisible = false,
                                    ),
                                ),
                        ),
                    isRefreshing = false,
                    isLoading = false,
                    hasError = false,
                ),
            onAction = {},
            goToCreateAdditionScreen = {},
            onBackClick = {},
        )
    }
}
