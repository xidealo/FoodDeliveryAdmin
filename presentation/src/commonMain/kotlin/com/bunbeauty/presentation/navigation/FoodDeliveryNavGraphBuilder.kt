package com.bunbeauty.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navOptions
import com.bunbeauty.presentation.feature.additiongrouplist.navigation.additionGroupListScreenRoute
import com.bunbeauty.presentation.feature.additionlist.navigation.additionListScreenRoute
import com.bunbeauty.presentation.feature.category.navigation.categoryListScreenRoute
import com.bunbeauty.presentation.feature.login.navigation.loginScreenRoute
import com.bunbeauty.presentation.feature.orderlist.navigation.orderListScreenRoute
import com.bunbeauty.presentation.feature.menulist.navigation.menuListScreenRoute
import com.bunbeauty.presentation.feature.menu.navigation.menuScreenRoute
import com.bunbeauty.presentation.feature.profile.navigation.profileScreenRoute
import com.bunbeauty.presentation.feature.settings.navigation.navigateToSettingsScreen
import com.bunbeauty.presentation.feature.settings.navigation.settingsScreenRoute
import com.bunbeauty.presentation.feature.statistic.navigation.navigateToStatisticScreen
import com.bunbeauty.presentation.feature.statistic.navigation.statisticScreenRoute

internal val emptyNavOptions = navOptions { }

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.foodDeliveryNavGraphBuilder(
    navController: NavController,
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
) {
    loginScreenRoute(
        showErrorMessage = showErrorMessage,
        goToOrderListScreen = {
            //navigate to order list
        },
    )
    menuScreenRoute(
        goToCategoriesScreen = {
            //navigate to categories
        },
        goToMenuListScreen = {
            //navigate to menu list
        },
        goToAdditionGroupListScreen = {
            //navigate to addition group list
        },
        goToAdditionListScreen = {
            //navigate to addition list
        },
    )
    menuListScreenRoute(
        goToCreateMenuProductScreen = {
            //navigate to create menu product
        },
        goToEditMenuProductScreen = { uuid ->
            //navigate to edit menu product
        },
    )
    additionGroupListScreenRoute(
        goBack = {
            //navigate back
        },
        goToCreateAdditionGroupScreen = {
            //navigate to create addition group
        },
        goToEditAdditionGroupScreen = { uuid ->
            //navigate to edit addition group
        },
    )
    additionListScreenRoute(
        goBack = {
            //navigate back
        },
        goToCreateAdditionScreen = {
            //navigate to create addition
        },
        goToEditAdditionScreen = { uuid ->
            //navigate to edit addition
        },
    )
    categoryListScreenRoute(
        goBack = {
            navController.popBackStack()
        },
        goToCreateCategoryScreen = {
            //navigate to create category
        },
        goToEditCategoryScreen = { categoryUuid ->
            //navigate to edit category
        },
    )
    profileScreenRoute(
        showErrorMessage = showErrorMessage,
        goToSettingsScreen = {
            navController.navigateToSettingsScreen(emptyNavOptions)
        },
        goToStatisticScreen = {
            navController.navigateToStatisticScreen(emptyNavOptions)
        },
        goToMapScreen = {
            //navigate to map
        },
        goToLoginScreen = {
            //navigate to login
        },
    )
    settingsScreenRoute(
        goBack = {
            navController.popBackStack()
        },
    )
    statisticScreenRoute(
        goBack = {
            navController.popBackStack()
        },
    )

    orderListScreenRoute(
        cancelNotification = cancelNotification,
        openOrderDetails =
            openOrderDetails
    )

}
