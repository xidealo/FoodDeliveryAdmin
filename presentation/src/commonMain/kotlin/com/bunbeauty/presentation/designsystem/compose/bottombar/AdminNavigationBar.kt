package com.bunbeauty.presentation.designsystem.compose.bottombar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.medium
import com.bunbeauty.presentation.viewmodel.main.Main
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.ic_menu
import fooddeliveryadmin.presentation.generated.resources.ic_orders
import fooddeliveryadmin.presentation.generated.resources.ic_profile
import fooddeliveryadmin.presentation.generated.resources.title_bottom_navigation_menu
import fooddeliveryadmin.presentation.generated.resources.title_bottom_navigation_orders
import fooddeliveryadmin.presentation.generated.resources.title_bottom_navigation_profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AdminNavigationBar(
    options: Main.NavigationBarOptions,
    goToOrderList: () -> Unit,
    goToMenu: () -> Unit,
    goToProfile: () -> Unit,
) {
    if (options is Main.NavigationBarOptions.Visible) {
        NavigationBar(
            modifier = Modifier.shadow(AdminTheme.dimensions.surfaceElevation),
            containerColor = AdminTheme.colors.main.surface,
        ) {
            FoodDeliveryBottomItem(
                selected = options.selectedItem == Main.NavigationBarItem.ORDERS,
                iconId = Res.drawable.ic_orders,
                labelId = Res.string.title_bottom_navigation_orders,
                onClick = goToOrderList,
            )
            FoodDeliveryBottomItem(
                selected = options.selectedItem == Main.NavigationBarItem.MENU,
                iconId = Res.drawable.ic_menu,
                labelId = Res.string.title_bottom_navigation_menu,
                onClick = goToMenu,
            )
            FoodDeliveryBottomItem(
                selected = options.selectedItem == Main.NavigationBarItem.PROFILE,
                iconId = Res.drawable.ic_profile,
                labelId = Res.string.title_bottom_navigation_profile,
                onClick = goToProfile,
            )
        }
    }
}

@Composable
private fun RowScope.FoodDeliveryBottomItem(
    selected: Boolean,
    iconId: DrawableResource,
    labelId: StringResource,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = {
            if (!selected) {
                onClick()
            }
        },
        label = {
            Text(
                text = stringResource(labelId),
                style = AdminTheme.typography.labelMedium.medium,
            )
        },
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(iconId),
                contentDescription = null,
            )
        },
        colors = FoodDeliveryNavigationBarDefaults.navigationBarItemColors(),
    )
}
