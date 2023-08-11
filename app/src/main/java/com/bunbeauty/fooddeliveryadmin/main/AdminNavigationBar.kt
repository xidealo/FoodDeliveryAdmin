package com.bunbeauty.fooddeliveryadmin.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.view_model.main.AdminNavigationBarItem
import com.bunbeauty.presentation.view_model.main.NavigationBarOptions

@Composable
fun AdminNavigationBar(options: NavigationBarOptions) {
    if (options is NavigationBarOptions.Visible) {
        NavigationBar(
            modifier = Modifier.shadow(AdminTheme.dimensions.surfaceElevation),
            containerColor = AdminTheme.colors.main.surface
        ) {
            FoodDeliveryBottomItem(
                selected = options.selectedItem == AdminNavigationBarItem.ORDERS,
                iconId = R.drawable.ic_orders,
                labelId = R.string.title_bottom_navigation_orders,
                onClick = {
                    options.navController.navigateSafe(R.id.global_to_ordersFragment)
                }
            )
            FoodDeliveryBottomItem(
                selected = options.selectedItem == AdminNavigationBarItem.MENU,
                iconId = R.drawable.ic_menu,
                labelId = R.string.title_bottom_navigation_menu,
                onClick = {
                    options.navController.navigateSafe(R.id.global_to_menuFragment)
                }
            )
            FoodDeliveryBottomItem(
                selected = options.selectedItem == AdminNavigationBarItem.PROFILE,
                iconId = R.drawable.ic_profile,
                labelId = R.string.title_bottom_navigation_profile,
                onClick = {
                    options.navController.navigateSafe(R.id.global_to_profileFragment)
                }
            )
        }
    }
}

@Composable
private fun RowScope.FoodDeliveryBottomItem(
    selected: Boolean,
    @DrawableRes iconId: Int,
    @StringRes labelId: Int,
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
                style = AdminTheme.typography.labelMedium.medium
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
