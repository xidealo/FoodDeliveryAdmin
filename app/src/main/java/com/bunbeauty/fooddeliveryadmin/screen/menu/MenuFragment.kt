package com.bunbeauty.fooddeliveryadmin.screen.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationIconCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.menu.Menu
import com.bunbeauty.presentation.feature.menu.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment :
    BaseComposeFragment<Menu.ViewDataState, MenuViewState, Menu.Action, Menu.Event>() {

    override val viewModel: MenuViewModel by viewModels()

    @Composable
    override fun Screen(state: MenuViewState, onAction: (Menu.Action) -> Unit) {
        MenuScreen(state = state, onAction = onAction)
    }

    @Composable
    private fun MenuScreen(state: MenuViewState, onAction: (Menu.Action) -> Unit) {
        AdminScaffold(
            title = stringResource(R.string.title_bottom_navigation_menu)
        ) {
            if (state.error != null) {
                ErrorScreen(mainTextId = R.string.error_common_loading_failed) {
                }
            } else {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NavigationIconCard(
                        iconId = R.drawable.ic_products,
                        labelStringId = R.string.action_menu_positions,
                        onClick = {
                            onAction(Menu.Action.OnMenuListClick)
                        }
                    )
                    NavigationIconCard(
                        iconId = R.drawable.ic_addition_group,
                        labelStringId = R.string.action_menu_addition_groups,
                        onClick = {
                            onAction(Menu.Action.OnAdditionGroupsListClick)
                        }
                    )
                    NavigationIconCard(
                        iconId = R.drawable.ic_addition,
                        labelStringId = R.string.action_menu_additions,
                        onClick = {
                            onAction(Menu.Action.OnAdditionsListClick)
                        }
                    )
                }
            }
        }
    }

    @Composable
    override fun mapState(state: Menu.ViewDataState): MenuViewState {
        return MenuViewState(error = state.error)
    }

    override fun handleEvent(event: Menu.Event) {
        when (event) {
            Menu.Event.OnMenuListClick -> {
                findNavController().navigateSafe(MenuFragmentDirections.toMenuListProductFragment())
            }

            Menu.Event.OnAdditionGroupsListClick -> {
            }

            Menu.Event.OnAdditionsListClick -> {
                findNavController().navigateSafe(MenuFragmentDirections.toAdditionListProductFragment())
            }
        }
    }

    @Preview
    @Composable
    private fun MenuScreenPreview() {
        AdminTheme {
            MenuScreen(state = MenuViewState(error = null), onAction = {})
        }
    }
}
