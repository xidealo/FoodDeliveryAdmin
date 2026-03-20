package com.bunbeauty.presentation.feature.menulist

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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.FloatingButton
import com.bunbeauty.presentation.designsystem.compose.element.card.AdminCard
import com.bunbeauty.presentation.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.presentation.designsystem.compose.element.image.ImageData
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.bold
import com.bunbeauty.presentation.model.MenuListEvent
import com.bunbeauty.presentation.model.MenuListViewState
import com.bunbeauty.presentation.model.MenuProductItem
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_menu_list_create
import fooddeliveryadmin.presentation.generated.resources.default_product
import fooddeliveryadmin.presentation.generated.resources.description_product
import fooddeliveryadmin.presentation.generated.resources.ic_plus
import fooddeliveryadmin.presentation.generated.resources.ic_visible
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.title_bottom_navigation_menu
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_menu_list_position_hidden
import fooddeliveryadmin.presentation.generated.resources.title_menu_list_position_visible
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val VISIBLE_TITLE_KEY = "visible title"
private const val HIDDEN_TITLE_KEY = "hidden title"
private const val LIST_ANIMATION_DURATION = 500

@Composable
fun MenuListRouteScreen(
    viewModel: MenuListViewModel = koinViewModel(),
    goToCreateMenuProductScreen: () -> Unit,
    goToEditMenuProductScreen: (String) -> Unit,
    back: () -> Unit,
) {
    val menuViewState by viewModel.menuState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(menuViewState.eventList) {
        menuViewState.eventList.forEach { event ->
            when (event) {
                is MenuListEvent.GoToEditMenuProductList -> {
                    goToEditMenuProductScreen(event.uuid)
                }
            }
        }
        viewModel.consumeEvents(menuViewState.eventList)
    }

    MenuListScreen(
        menuListViewState = menuViewState,
        goToCreateMenuProductScreen = goToCreateMenuProductScreen,
        onProductClick = { uuid ->
            viewModel.goToEditMenuProduct(uuid)
        },
        onUpdateVisible = { menuProduct ->
            viewModel.updateVisible(menuProduct)
        },
        onLoadData = {
            viewModel.loadData()
        },
        onRefresh = {
            viewModel.refreshData()
        },
        back = back
    )
}

@Composable
private fun MenuListScreen(
    menuListViewState: MenuListViewState,
    goToCreateMenuProductScreen: () -> Unit,
    onProductClick: (String) -> Unit,
    onUpdateVisible: (MenuProductItem) -> Unit,
    onLoadData: () -> Unit,
    onRefresh: () -> Unit,
    back: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_bottom_navigation_menu),
        pullRefreshEnabled = true,
        refreshing = menuListViewState.isRefreshing,
        onRefresh = onRefresh,
        backActionClick = back,
        actionButton = {
            if (menuListViewState.state is MenuListViewState.State.Success) {
                FloatingButton(
                    iconId = Res.drawable.ic_plus,
                    textStringId = Res.string.action_menu_list_create,
                    onClick = goToCreateMenuProductScreen,
                )
            }
        },
        floatingActionButtonPosition =  Alignment.BottomEnd,
    ) {
        when (val state = menuListViewState.state) {
            is MenuListViewState.State.Success -> {
                MenuListSuccessScreen(state, onProductClick, onUpdateVisible)
            }

            is MenuListViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = onLoadData,
                )
            }

            MenuListViewState.State.Loading -> {
                LoadingScreen()
            }
        }
    }
}

@Composable
private fun MenuListSuccessScreen(
    state: MenuListViewState.State.Success,
    onProductClick: (String) -> Unit,
    onUpdateVisible: (MenuProductItem) -> Unit,
) {
    LazyColumn(
        contentPadding =
            PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (state.visibleMenuProductItems.isNotEmpty()) {
            item(key = VISIBLE_TITLE_KEY) {
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
                items = state.visibleMenuProductItems,
                key = { visibleMenuProduct ->
                    visibleMenuProduct.uuid
                },
            ) { visibleMenuProduct ->
                MenuListProductCard(
                    modifier =
                        Modifier
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION),
                            ),
                    menuProduct = visibleMenuProduct,
                    onProductClick = onProductClick,
                    onUpdateVisible = onUpdateVisible,
                )
            }
        }
        if (state.hiddenMenuProductItems.isNotEmpty()) {
            item(key = HIDDEN_TITLE_KEY) {
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
                items = state.hiddenMenuProductItems,
                key = { visibleMenuProduct ->
                    visibleMenuProduct.uuid
                },
            ) { hiddenMenuProduct ->
                MenuListProductCard(
                    modifier =
                        Modifier
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION),
                            ),
                    menuProduct = hiddenMenuProduct,
                    onProductClick = onProductClick,
                    onUpdateVisible = onUpdateVisible,
                )
            }
        }
    }
}

@Composable
private fun MenuListProductCard(
    menuProduct: MenuProductItem,
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit,
    onUpdateVisible: (MenuProductItem) -> Unit,
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onProductClick(menuProduct.uuid)
        },
        elevated = false,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AdminAsyncImage(
                imageData = ImageData.HttpUrl(menuProduct.photoLink),
                contentDescription = Res.string.description_product,
                placeholder = Res.drawable.default_product,
                modifier =
                    Modifier
                        .width(AdminTheme.dimensions.productImageSmallWidth)
                        .height(AdminTheme.dimensions.productImageSmallHeight),
            )

            Text(
                text = menuProduct.name,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(top = AdminTheme.dimensions.smallSpace)
                        .padding(horizontal = AdminTheme.dimensions.smallSpace),
            )

            IconButton(
                modifier =
                    Modifier
                        .align(CenterVertically)
                        .padding(end = AdminTheme.dimensions.smallSpace),
                onClick = {
                    onUpdateVisible(menuProduct)
                },
            ) {
                Icon(
                    painter =
                        org.jetbrains.compose.resources
                            .painterResource(Res.drawable.ic_visible),
                    contentDescription = null,
                    tint =
                        if (menuProduct.visible) {
                            AdminTheme.colors.main.primary
                        } else {
                            AdminTheme.colors.main.onSurfaceVariant
                        },
                )
            }
        }
    }
}

@Preview
@Composable
private fun MenuListScreenPreview() {
    AdminTheme {
        MenuListScreen(
            menuListViewState =
                MenuListViewState(
                    state =
                        MenuListViewState.State.Success(
                            visibleMenuProductItems =
                                listOf(
                                    MenuProductItem(
                                        name = "name",
                                        photoLink = "",
                                        visible = true,
                                        uuid = "asdasd",
                                    ),
                                    MenuProductItem(
                                        name = "name 1 ",
                                        photoLink = "",
                                        visible = true,
                                        uuid = "asdasd",
                                    ),
                                    MenuProductItem(
                                        name = "name 2 32423423412ыфвафва вфавафвыафвыавыф3",
                                        photoLink = "",
                                        visible = true,
                                        uuid = "asdasd",
                                    ),
                                ),
                            hiddenMenuProductItems =
                                listOf(
                                    MenuProductItem(
                                        name = "name",
                                        photoLink = "",
                                        visible = true,
                                        uuid = "asdasd",
                                    ),
                                    MenuProductItem(
                                        name = "name 1 ",
                                        photoLink = "",
                                        visible = true,
                                        uuid = "asdasd",
                                    ),
                                    MenuProductItem(
                                        name = "name 2 32423423412ыфвафва вфавафвыафвыавыф3",
                                        photoLink = "",
                                        visible = true,
                                        uuid = "asdasd",
                                    ),
                                ),
                        ),
                    isRefreshing = false,
                    eventList = emptyList(),
                ),
            goToCreateMenuProductScreen = {},
            onProductClick = {},
            onUpdateVisible = {},
            onLoadData = {},
            onRefresh = {},
            back = {}
        )
    }
}
