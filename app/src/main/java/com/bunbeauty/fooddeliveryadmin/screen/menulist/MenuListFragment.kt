package com.bunbeauty.fooddeliveryadmin.screen.menulist

import android.os.Bundle
import android.view.View
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.FloatingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.menulist.MenuListViewModel
import com.bunbeauty.presentation.model.MenuListEvent
import com.bunbeauty.presentation.model.MenuListViewState
import com.bunbeauty.presentation.model.MenuProductItem
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val VISIBLE_TITLE_KEY = "visible title"
private const val HIDDEN_TITLE_KEY = "hidden title"
private const val LIST_ANIMATION_DURATION = 500

class MenuListFragment : BaseFragment<LayoutComposeBinding>() {

    override val viewModel: MenuListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()

        binding.root.setContentWithTheme {
            val menuViewState by viewModel.menuState.collectAsStateWithLifecycle()
            MenuListScreen(menuListViewState = menuViewState)
            LaunchedEffect(menuViewState.eventList) {
                handleEventList(menuViewState.eventList)
            }
        }
    }

    private fun handleEventList(eventList: List<MenuListEvent>) {
        eventList.forEach { event ->
            when (event) {
                is MenuListEvent.GoToEditMenuProductList -> {
                    findNavController().navigateSafe(
                        MenuListFragmentDirections.toEditMenuProductFragment(event.uuid)
                    )
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }

    @Composable
    private fun MenuListScreen(menuListViewState: MenuListViewState) {
        AdminScaffold(
            title = stringResource(R.string.title_bottom_navigation_menu),
            pullRefreshEnabled = true,
            refreshing = menuListViewState.isRefreshing,
            onRefresh = viewModel::refreshData,
            backActionClick = { findNavController().popBackStack() },
            actionButton = {
                if (menuListViewState.state is MenuListViewState.State.Success) {
                    FloatingButton(
                        iconId = R.drawable.ic_plus,
                        textStringId = R.string.action_menu_list_create,
                        onClick = {
                            findNavController().navigateSafe(MenuListFragmentDirections.toCreateMenuProductFragment())
                        }
                    )
                }
            },
            actionButtonPosition = FabPosition.End
        ) {
            when (val state = menuListViewState.state) {
                is MenuListViewState.State.Success -> {
                    MenuListSuccessScreen(state)
                }

                is MenuListViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = viewModel::loadData
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
        state: MenuListViewState.State.Success
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.visibleMenuProductItems.isNotEmpty()) {
                item(key = VISIBLE_TITLE_KEY) {
                    Text(
                        modifier = Modifier
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION)
                            ),
                        text = stringResource(id = R.string.title_menu_list_position_visible),
                        style = AdminTheme.typography.titleMedium.bold
                    )
                }
                items(
                    items = state.visibleMenuProductItems,
                    key = { visibleMenuProduct ->
                        visibleMenuProduct.uuid
                    }
                ) { visibleMenuProduct ->
                    MenuListProductCard(
                        modifier = Modifier
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION)
                            ),
                        menuProduct = visibleMenuProduct
                    )
                }
            }
            if (state.hiddenMenuProductItems.isNotEmpty()) {
                item(key = HIDDEN_TITLE_KEY) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION)
                            ),
                        text = stringResource(id = R.string.title_menu_list_position_hidden),
                        style = AdminTheme.typography.titleMedium.bold
                    )
                }
                items(
                    items = state.hiddenMenuProductItems,
                    key = { visibleMenuProduct ->
                        visibleMenuProduct.uuid
                    }
                ) { hiddenMenuProduct ->
                    MenuListProductCard(
                        modifier = Modifier
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION)
                            ),
                        menuProduct = hiddenMenuProduct
                    )
                }
            }
        }
    }

    @Composable
    private fun MenuListProductCard(
        menuProduct: MenuProductItem,
        modifier: Modifier = Modifier
    ) {
        AdminCard(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                viewModel.goToEditMenuProduct(menuProduct.uuid)
            },
            elevated = false
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(AdminTheme.dimensions.productImageSmallWidth)
                        .height(AdminTheme.dimensions.productImageSmallHeight),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(menuProduct.photoLink)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.default_product),
                    contentDescription = stringResource(R.string.description_product),
                    contentScale = ContentScale.FillWidth
                )

                Text(
                    text = menuProduct.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = AdminTheme.dimensions.smallSpace)
                        .padding(horizontal = AdminTheme.dimensions.smallSpace)
                )

                IconButton(
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(end = AdminTheme.dimensions.smallSpace),
                    onClick = {
                        viewModel.updateVisible(menuProduct)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_visible),
                        contentDescription = null,
                        tint = if (menuProduct.visible) {
                            AdminTheme.colors.main.primary
                        } else {
                            AdminTheme.colors.main.onSurfaceVariant
                        }
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
                menuListViewState = MenuListViewState(
                    state = MenuListViewState.State.Success(
                        visibleMenuProductItems = listOf(
                            MenuProductItem(
                                name = "name",
                                photoLink = "",
                                visible = true,
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 1 ",
                                photoLink = "",
                                visible = true,
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 2 32423423412ыфвафва вфавафвыафвыавыф3",
                                photoLink = "",
                                visible = true,
                                uuid = "asdasd"
                            )
                        ),
                        hiddenMenuProductItems = listOf(
                            MenuProductItem(
                                name = "name",
                                photoLink = "",
                                visible = true,
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 1 ",
                                photoLink = "",
                                visible = true,
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 2 32423423412ыфвафва вфавафвыафвыавыф3",
                                photoLink = "",
                                visible = true,
                                uuid = "asdasd"
                            )
                        )
                    ),
                    isRefreshing = false,
                    eventList = emptyList()
                )
            )
        }
    }
}
