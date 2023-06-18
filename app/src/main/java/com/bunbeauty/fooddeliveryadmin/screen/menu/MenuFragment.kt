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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentComposeBinding
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.util.compose
import com.bunbeauty.presentation.model.MenuState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentComposeBinding>() {

    override val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.compose {
            val menuViewState by viewModel.menuState.collectAsStateWithLifecycle()
            MenuScreen(menuState = menuViewState)
        }
    }

    @Composable
    fun MenuScreen(menuState: MenuState) {
        AdminScaffold(
            title = stringResource(R.string.title_bottom_navigation_menu),
            pullRefreshEnabled = true,
            refreshing = menuState.isRefreshing,
            onRefresh = viewModel::refreshData,
        ) {
            if (menuState.throwable == null) {
                MenuSuccessScreen(menuState)
            } else {
                MenuErrorScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MenuSuccessScreen(menuState: MenuState) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuState.menuProductItems) { menuProduct ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        findNavController().navigateSafe(
                            MenuFragmentDirections.toEditMenuProductFragment(menuProduct.uuid)
                        )
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                tint = AdminTheme.colors.mainColors.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        if (menuState.isLoading) {
            LinearProgressIndicator(
                color = AdminTheme.colors.mainColors.primary,
                modifier = Modifier.fillMaxWidth(),
            )
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
                menuState = MenuState(
                    menuProductItems = listOf(
                        MenuState.MenuProductItem(
                            name = "name",
                            photoLink = "",
                            visible = true,
                            newCost = "500",
                            oldCost = "1000",
                            uuid = "asdasd"
                        ),
                        MenuState.MenuProductItem(
                            name = "name 1 ",
                            photoLink = "",
                            visible = true,
                            newCost = "500",
                            oldCost = "1000",
                            uuid = "asdasd"
                        ),
                        MenuState.MenuProductItem(
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
}
