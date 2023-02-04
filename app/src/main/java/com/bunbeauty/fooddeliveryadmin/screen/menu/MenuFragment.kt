package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    @Composable
    fun MenuScreen(menuViewState: MenuViewState) {
        //val state = rememberPullRefreshState(menuViewState.isLoading, pullToRefresh)

        /* Box(Modifier.pullRefresh(state)) {*/
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodDeliveryTheme.colors.background)
        ) {

            when {
                menuViewState.throwable != null -> {

                }
                else -> {
                    LazyColumn() {
                        items(menuViewState.menuProductItems) { menuProduct ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = FoodDeliveryTheme.dimensions.smallSpace)
                                    .padding(horizontal = FoodDeliveryTheme.dimensions.mediumSpace)
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
                                        }) {
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
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }
        }
/*
            PullRefreshIndicator(
                refreshing = menuViewState.isLoading,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = FoodDeliveryTheme.colors.primary
            )
        }*/

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
