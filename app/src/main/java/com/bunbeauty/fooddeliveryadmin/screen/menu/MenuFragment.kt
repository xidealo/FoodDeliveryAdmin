package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.model.MenuEvent
import com.bunbeauty.presentation.model.MenuProductItem
import com.bunbeauty.presentation.model.MenuUiState
import com.bunbeauty.presentation.view_model.menu.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<LayoutComposeBinding>() {

    override val viewModel: MenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()

        binding.root.setContentWithTheme {
            val menuViewState by viewModel.menuState.collectAsStateWithLifecycle()
            MenuScreen(menuUiState = menuViewState)
            LaunchedEffect(menuViewState.eventList) {
                handleEventList(menuViewState.eventList)
            }
        }
    }

    private fun handleEventList(eventList: List<MenuEvent>) {
        eventList.forEach { event ->
            when (event) {
                is MenuEvent.GoToEditMenuProduct -> {
                    findNavController().navigateSafe(
                        MenuFragmentDirections.toEditMenuProductFragment(event.uuid)
                    )
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }

    @Composable
    private fun MenuScreen(menuUiState: MenuUiState) {
        AdminScaffold(
            title = stringResource(R.string.title_bottom_navigation_menu),
            pullRefreshEnabled = true,
            refreshing = menuUiState.isRefreshing,
            onRefresh = viewModel::refreshData
        ) {
            when (val state = menuUiState.state) {
                is MenuUiState.State.Success -> {
                    MenuSuccessScreen(state)
                }

                is MenuUiState.State.Error -> {
                    MenuErrorScreen()
                }

                MenuUiState.State.Loading -> {
                    LoadingScreen()
                }
            }
        }
    }

    @Composable
    private fun MenuSuccessScreen(
        state: MenuUiState.State.Success
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.visibleMenuProductItems.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.title_menu_product_visible),
                        style = AdminTheme.typography.titleMedium.bold
                    )
                }
                items(state.visibleMenuProductItems) { visibleMenuProduct ->
                    MenuProductCard(visibleMenuProduct)
                }
            }
            if (state.hiddenMenuProductItems.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.title_menu_product_hidden),
                        style = AdminTheme.typography.titleMedium.bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(state.hiddenMenuProductItems) { hiddenMenuProduct ->
                    MenuProductCard(hiddenMenuProduct)
                }
            }
        }
    }

    @Composable
    private fun MenuProductCard(menuProduct: MenuProductItem) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.goToEditMenuProduct(menuProduct.uuid)
            }
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
                        painter = if (menuProduct.visible) {
                            R.drawable.ic_invisible
                        } else {
                            R.drawable.ic_visible
                        }.let { iconId ->
                            painterResource(iconId)
                        },
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

    @Composable
    private fun MenuErrorScreen() {
        ErrorScreen(
            mainTextId = R.string.title_common_can_not_load_data,
            extraTextId = R.string.msg_common_check_connection_and_retry,
            onClick = viewModel::loadData
        )
    }

    @Preview
    @Composable
    private fun MenuScreenPreview() {
        AdminTheme {
            MenuScreen(
                menuUiState = MenuUiState(
                    state = MenuUiState.State.Success(
                        visibleMenuProductItems = listOf(
                            MenuProductItem(
                                name = "name",
                                photoLink = "",
                                visible = true,
                                newCost = "500",
                                oldCost = "1000",
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 1 ",
                                photoLink = "",
                                visible = true,
                                newCost = "500",
                                oldCost = "1000",
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 2 32423423412ыфвафва вфавафвыафвыавыф3",
                                photoLink = "",
                                visible = true,
                                newCost = "500",
                                oldCost = "1000",
                                uuid = "asdasd"
                            )
                        ),
                        hiddenMenuProductItems = listOf(
                            MenuProductItem(
                                name = "name",
                                photoLink = "",
                                visible = true,
                                newCost = "500",
                                oldCost = "1000",
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 1 ",
                                photoLink = "",
                                visible = true,
                                newCost = "500",
                                oldCost = "1000",
                                uuid = "asdasd"
                            ),
                            MenuProductItem(
                                name = "name 2 32423423412ыфвафва вфавафвыафвыавыф3",
                                photoLink = "",
                                visible = true,
                                newCost = "500",
                                oldCost = "1000",
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
