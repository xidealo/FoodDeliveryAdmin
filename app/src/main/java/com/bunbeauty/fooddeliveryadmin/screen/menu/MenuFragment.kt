package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMenuBinding
import com.bunbeauty.fooddeliveryadmin.util.compose
import com.bunbeauty.fooddeliveryadmin.view.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.view.theme.FoodDeliveryTheme
import com.bunbeauty.presentation.model.MenuViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeViewMenu.compose {
            val menuViewState by viewModel.menuViewState.collectAsStateWithLifecycle()
            MenuScreen(menuViewState = menuViewState)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MenuScreen(menuViewState: MenuViewState) {
        val state = rememberPullRefreshState(
            menuViewState.isRefreshing,
            { viewModel.loadData(isRefreshing = true) }
        )

        Box(modifier = Modifier.pullRefresh(state)) {
            Column {
                TopAppBar(
                    title = {
                        Text(stringResource(id = R.string.title_bottom_navigation_menu_menu))
                    },
                    backgroundColor = FoodDeliveryTheme.colors.surface,
                    contentColor = FoodDeliveryTheme.colors.onSurface,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FoodDeliveryTheme.colors.background)
                ) {
                    when {
                        menuViewState.throwable != null -> {
                            ErrorScreen(
                                mainTextId = R.string.title_can_not_load_data,
                                iconId = R.drawable.ic_repeat,
                                extraTextId = R.string.msg_check_connection_and_retry
                            ) {
                                viewModel.loadData()
                            }
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .background(FoodDeliveryTheme.colors.background),
                                contentPadding = PaddingValues(
                                    start = FoodDeliveryTheme.dimensions.mediumSpace,
                                    end = FoodDeliveryTheme.dimensions.mediumSpace,
                                    bottom = FoodDeliveryTheme.dimensions.mediumSpace,
                                ),
                            ) {
                                items(menuViewState.menuProductItems) { menuProduct ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = FoodDeliveryTheme.dimensions.smallSpace)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            AsyncImage(
                                                modifier = Modifier
                                                    .width(FoodDeliveryTheme.dimensions.productImageSmallWidth)
                                                    .height(FoodDeliveryTheme.dimensions.productImageSmallHeight),
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
                                                    .padding(top = FoodDeliveryTheme.dimensions.smallSpace)
                                                    .padding(horizontal = FoodDeliveryTheme.dimensions.smallSpace)
                                            )

                                            IconButton(
                                                modifier = Modifier
                                                    .align(CenterVertically)
                                                    .padding(end = FoodDeliveryTheme.dimensions.smallSpace),
                                                onClick = {
                                                    viewModel.updateVisible(menuProduct)
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_visible),
                                                    contentDescription = null,
                                                    tint = if (menuProduct.visible) {
                                                        FoodDeliveryTheme.colors.primary
                                                    } else {
                                                        FoodDeliveryTheme.colors.primaryDisabled
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            if (menuViewState.isLoading) {
                                LinearProgressIndicator(
                                    color = FoodDeliveryTheme.colors.primary,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = menuViewState.isRefreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = FoodDeliveryTheme.colors.primary
            )
        }
    }

    @Preview
    @Composable
    fun MenuScreenPreview() {
        MenuScreen(
            menuViewState = MenuViewState(
                menuProductItems = listOf(
                    MenuViewState.MenuProductItem(
                        name = "name",
                        photoLink = "",
                        visible = true,
                        newCost = "500",
                        oldCost = "1000",
                        uuid = "asdasd"
                    ),
                    MenuViewState.MenuProductItem(
                        name = "name 1 ",
                        photoLink = "",
                        visible = true,
                        newCost = "500",
                        oldCost = "1000",
                        uuid = "asdasd"
                    ),
                    MenuViewState.MenuProductItem(
                        name = "name 2 32423423412ыфвафва вфавафвыафвыавыф3",
                        photoLink = "",
                        visible = true,
                        newCost = "500",
                        oldCost = "1000",
                        uuid = "asdasd"
                    ),
                ),
                isLoading = false
            )
        )
    }
}
