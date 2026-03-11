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
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.navigation.navigateToEditDeliveryZoneInfoScreen
import com.bunbeauty.presentation.feature.mapdelivery.navigation.mapDeliveryZoneScreenRoute
import com.bunbeauty.presentation.feature.menu.navigation.menuScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.navigation.createAdditionGroupForMenuProductScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.navigation.navigateToCreateAdditionGroupForMenuProductScreen
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.navigation.editAdditionGroupForMenuProductScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.navigation.navigateToEditAdditionGroupForMenuProductScreen
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.navigation.additionGroupForMenuProductListScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.navigation.navigateToSelectAdditionListScreen
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.navigation.selectAdditionListScreenRoute
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.navigation.navigateToSelectAdditionGroupScreen
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.navigation.selectAdditionGroupScreenRoute
import com.bunbeauty.presentation.feature.menulist.categorylist.navigation.selectCategoryListScreenRoute
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.navigation.createMenuProductScreenRoute
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
            //navigate to create menu product - handled by parent routing
        },
        goToEditMenuProductScreen = { uuid ->
            //navigate to edit menu product
        },
    )
    createMenuProductScreenRoute(
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = {
            navController.popBackStack()
        },
        goToCategoryList = { selectedCategoryList ->
            //navigate to category list - handled by parent
        },
        goToCropImage = { imageUri ->
            //navigate to crop image - handled by parent
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
    mapDeliveryZoneScreenRoute(
        goBack = {
            navController.popBackStack()
        },
        goToEditDeliveryZoneInfo = { zoneUuid ->
            navController.navigateToEditDeliveryZoneInfoScreen(
                zoneUuid = zoneUuid,
                navOptions = emptyNavOptions,
            )
        },
        onZoneUpdated = { zoneUuid ->
            // Zone updated - handled by map screen via onAction
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
        goToSelectAdditionGroup = { uuid, menuProductUuid, mainEditedAdditionGroupUuid ->
            navController.navigateToSelectAdditionGroupScreen(
                additionGroupUuid = uuid,
                menuProductUuid = menuProductUuid,
                mainEditedAdditionGroupUuid = mainEditedAdditionGroupUuid,
                navOptions = emptyNavOptions,
            )
        },
        goToSelectAdditionList = { additionGroupUuid, menuProductUuid, groupName, addListUuid ->
            navController.navigateToSelectAdditionListScreen(
                menuProductUuid = menuProductUuid,
                additionGroupUuid = additionGroupUuid,
                additionGroupName = groupName,
                editedAdditionListUuid = addListUuid,
                navOptions = emptyNavOptions,
            )
        },
    )
    editAdditionGroupForMenuProductScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
        goToSelectAdditionGroup = { editedAdditionGroupUuid, menuProductUuid, mainEditedAdditionGroupUuid ->
            navController.navigateToSelectAdditionGroupScreen(
                additionGroupUuid = editedAdditionGroupUuid,
                menuProductUuid = menuProductUuid,
                mainEditedAdditionGroupUuid = mainEditedAdditionGroupUuid,
                navOptions = emptyNavOptions,
            )
        },
        goToSelectAdditionList = { additionGroupUuid, menuProductUuid, additionGroupName, editedAdditionListUuid ->
            navController.navigateToSelectAdditionListScreen(
                menuProductUuid = menuProductUuid,
                additionGroupUuid = additionGroupUuid,
                additionGroupName = additionGroupName,
                editedAdditionListUuid = editedAdditionListUuid,
                navOptions = emptyNavOptions,
            )
        },
    )
    selectAdditionGroupScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = {
            navController.popBackStack()
        },
        onAdditionGroupSelected = { additionGroupUuid, additionGroupName ->
            // Result handled by calling screen
        },
    )
    selectAdditionListScreenRoute(
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = {
            navController.popBackStack()
        },
        onAdditionListSelected = { additionUuidList ->
            // Result handled by calling screen
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
            //navigate to map - handled by existing navigation
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
