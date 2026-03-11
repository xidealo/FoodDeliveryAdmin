package com.bunbeauty.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navOptions
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.navigation.createAdditionGroupScreenRoute
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.navigation.navigateToCreateAdditionGroupScreen
import com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup.navigation.editAdditionGroupScreenRoute
import com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup.navigation.navigateToEditAdditionGroupScreen
import com.bunbeauty.presentation.feature.additiongrouplist.navigation.additionGroupListScreenRoute
import com.bunbeauty.presentation.feature.additionlist.navigation.additionListScreenRoute
import com.bunbeauty.presentation.feature.category.createcategory.navigation.createCategoryScreenRoute
import com.bunbeauty.presentation.feature.category.createcategory.navigation.navigateToCreateCategoryScreen
import com.bunbeauty.presentation.feature.category.editcategory.navigation.editCategoryScreenRoute
import com.bunbeauty.presentation.feature.category.editcategory.navigation.navigateToEditCategoryScreen
import com.bunbeauty.presentation.feature.category.navigation.categoryListScreenRoute
import com.bunbeauty.presentation.feature.gallery.navigation.galleryScreenRoute
import com.bunbeauty.presentation.feature.login.navigation.loginScreenRoute
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.navigation.editDeliveryZoneInfoScreenRoute
import com.bunbeauty.presentation.feature.menu.navigation.menuScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.navigation.createAdditionGroupForMenuProductScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.navigation.navigateToCreateAdditionGroupForMenuProductScreen
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.navigation.additionGroupForMenuProductListScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.navigation.editAdditionGroupForMenuProductScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.navigation.navigateToEditAdditionGroupForMenuProductScreen
import com.bunbeauty.presentation.feature.menulist.categorylist.navigation.selectCategoryListScreenRoute
import com.bunbeauty.presentation.feature.menulist.cropimage.navigation.cropImageScreenRoute
import com.bunbeauty.presentation.feature.menulist.navigation.menuListScreenRoute
import com.bunbeauty.presentation.feature.orderlist.navigation.orderListScreenRoute
import com.bunbeauty.presentation.feature.profile.navigation.profileScreenRoute
import com.bunbeauty.presentation.feature.settings.navigation.navigateToSettingsScreen
import com.bunbeauty.presentation.feature.settings.navigation.settingsScreenRoute
import com.bunbeauty.presentation.feature.statistic.navigation.navigateToStatisticScreen
import com.bunbeauty.presentation.feature.statistic.navigation.statisticScreenRoute
import com.bunbeauty.presentation.feature.statisticdetails.navigation.statisticDetailsScreenRoute

internal val emptyNavOptions = navOptions { }

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.foodDeliveryNavGraphBuilder(
    navController: NavController,
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    cancelNotification: (Int) -> Unit,
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
            navController.navigateToCreateAdditionGroupScreen(emptyNavOptions)
        },
        goToEditAdditionGroupScreen = { uuid ->
            navController.navigateToEditAdditionGroupScreen(
                additionGroupUuid = uuid,
                navOptions = emptyNavOptions,
            )
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
            navController.navigateToCreateCategoryScreen(emptyNavOptions)
        },
        goToEditCategoryScreen = { categoryUuid ->
            navController.navigateToEditCategoryScreen(
                categoryUuid = categoryUuid,
                navOptions = emptyNavOptions,
            )
        },
    )
    createCategoryScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
    )
    editCategoryScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
    )
    createAdditionGroupScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
    )
    editAdditionGroupScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
    )
    editDeliveryZoneInfoScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
    )
    galleryScreenRoute(
        goBack = {
            navController.popBackStack()
        },
    )
    statisticDetailsScreenRoute(
        goBack = {
            navController.popBackStack()
        },
    )
    selectCategoryListScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
    )
    cropImageScreenRoute(
        goBack = {
            navController.popBackStack()
        },
    )
    additionGroupForMenuProductListScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
        goToCreateAdditionGroup = { menuProductUuid ->
            navController.navigateToCreateAdditionGroupForMenuProductScreen(
                menuProductUuid = menuProductUuid,
                navOptions = emptyNavOptions,
            )
        },
        goToEditAdditionGroup = { menuProductUuid, additionGroupUuid ->
            navController.navigateToEditAdditionGroupForMenuProductScreen(
                additionGroupUuid = additionGroupUuid,
                menuProductUuid = menuProductUuid,
                navOptions = emptyNavOptions,
            )
        },
    )
    createAdditionGroupForMenuProductScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
        goToSelectAdditionGroup = { uuid, menuProductUuid, _ ->
            //navigate to select addition group
        },
        goToSelectAdditionList = { _, menuProductUuid, groupName, addListUuid ->
            //navigate to select addition list
        },
    )
    editAdditionGroupForMenuProductScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
        goToSelectAdditionGroup = { _, _, _ ->
            //navigate to select addition group
        },
        goToSelectAdditionList = { _, _, _, _ ->
            //navigate to select addition list
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
        openOrderDetails = { string, string1 ->
        }
    )

}
