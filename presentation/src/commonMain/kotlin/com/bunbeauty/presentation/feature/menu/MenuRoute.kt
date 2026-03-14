package com.bunbeauty.presentation.feature.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.card.NavigationIconCard
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_menu_addition_groups
import fooddeliveryadmin.presentation.generated.resources.action_menu_additions
import fooddeliveryadmin.presentation.generated.resources.action_menu_categories
import fooddeliveryadmin.presentation.generated.resources.action_menu_positions
import fooddeliveryadmin.presentation.generated.resources.ic_addition
import fooddeliveryadmin.presentation.generated.resources.ic_addition_group
import fooddeliveryadmin.presentation.generated.resources.ic_categories
import fooddeliveryadmin.presentation.generated.resources.ic_products
import fooddeliveryadmin.presentation.generated.resources.title_bottom_navigation_menu
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MenuRouteScreen(
    viewModel: MenuViewModel = koinViewModel(),
    goToCategoriesScreen: () -> Unit,
    goToMenuListScreen: () -> Unit,
    goToAdditionGroupListScreen: () -> Unit,
    goToAdditionListScreen: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: Menu.Action ->
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

    MenuEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goToCategoriesScreen = goToCategoriesScreen,
        goToMenuListScreen = goToMenuListScreen,
        goToAdditionGroupListScreen = goToAdditionGroupListScreen,
        goToAdditionListScreen = goToAdditionListScreen,
    )

    MenuScreen(onAction = onAction)
}

@Composable
private fun MenuEffect(
    effects: List<Menu.Event>,
    goToCategoriesScreen: () -> Unit,
    goToMenuListScreen: () -> Unit,
    goToAdditionGroupListScreen: () -> Unit,
    goToAdditionListScreen: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                Menu.Event.OnCategoriesListClick -> {
                    goToCategoriesScreen()
                }

                Menu.Event.OnMenuListClick -> {
                    goToMenuListScreen()
                }

                Menu.Event.OnAdditionGroupsListClick -> {
                    goToAdditionGroupListScreen()
                }

                Menu.Event.OnAdditionsListClick -> {
                    goToAdditionListScreen()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun MenuScreen(onAction: (Menu.Action) -> Unit) {
    AdminScaffold(
        title = stringResource(Res.string.title_bottom_navigation_menu),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NavigationIconCard(
                iconId = Res.drawable.ic_categories,
                labelStringId = Res.string.action_menu_categories,
                onClick = {
                    onAction(Menu.Action.OnCategoriesListClick)
                },
            )
            NavigationIconCard(
                iconId = Res.drawable.ic_products,
                labelStringId = Res.string.action_menu_positions,
                onClick = {
                    onAction(Menu.Action.OnMenuListClick)
                },
            )
            NavigationIconCard(
                iconId = Res.drawable.ic_addition_group,
                labelStringId = Res.string.action_menu_addition_groups,
                onClick = {
                    onAction(Menu.Action.OnAdditionGroupsListClick)
                },
            )
            NavigationIconCard(
                iconId = Res.drawable.ic_addition,
                labelStringId = Res.string.action_menu_additions,
                onClick = {
                    onAction(Menu.Action.OnAdditionsListClick)
                },
            )
        }
    }
}

@Preview
@Composable
private fun MenuScreenPreview() {
    AdminTheme {
        MenuScreen(onAction = {})
    }
}
