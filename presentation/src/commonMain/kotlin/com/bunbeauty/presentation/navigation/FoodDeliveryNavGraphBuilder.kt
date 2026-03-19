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
import com.bunbeauty.presentation.feature.additiongrouplist.navigation.navigateToAdditionGroupListScreen
import com.bunbeauty.presentation.feature.additionlist.createaddition.navigation.createAdditionScreenRoute
import com.bunbeauty.presentation.feature.additionlist.createaddition.navigation.navigateToCreateAdditionScreen
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.navigation.editAdditionScreenRoute
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.navigation.navigateToEditAdditionScreen
import com.bunbeauty.presentation.feature.additionlist.navigation.additionListScreenRoute
import com.bunbeauty.presentation.feature.additionlist.navigation.navigateToAdditionListScreen
import com.bunbeauty.presentation.feature.category.createcategory.navigation.createCategoryScreenRoute
import com.bunbeauty.presentation.feature.category.createcategory.navigation.navigateToCreateCategoryScreen
import com.bunbeauty.presentation.feature.category.editcategory.navigation.editCategoryScreenRoute
import com.bunbeauty.presentation.feature.category.editcategory.navigation.navigateToEditCategoryScreen
import com.bunbeauty.presentation.feature.category.navigation.categoryListScreenRoute
import com.bunbeauty.presentation.feature.category.navigation.navigateToCategoryListScreen
import com.bunbeauty.presentation.feature.gallery.navigation.galleryScreenRoute
import com.bunbeauty.presentation.feature.login.navigation.loginScreenRoute
import com.bunbeauty.presentation.feature.login.navigation.navigateToLoginScreen
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
import com.bunbeauty.presentation.feature.menulist.categorylist.SELECTED_CATEGORY_UUID_LIST
import com.bunbeauty.presentation.feature.menulist.categorylist.navigation.navigateToSelectCategoryListScreen
import com.bunbeauty.presentation.feature.menulist.categorylist.navigation.selectCategoryListScreenRoute
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.CreateMenuProduct
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.navigation.createMenuProductScreenRoute
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.navigation.navigateToCreateMenuProductScreen
import com.bunbeauty.presentation.feature.menulist.cropimage.navigation.cropImageScreenRoute
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.navigation.editMenuProductScreenRoute
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.navigation.navigateToEditMenuProductScreen
import com.bunbeauty.presentation.feature.menulist.navigation.menuListScreenRoute
import com.bunbeauty.presentation.feature.menulist.navigation.navigateToMenuListScreen
import com.bunbeauty.presentation.feature.order.navigation.navigateToOrderDetailsScreen
import com.bunbeauty.presentation.feature.order.navigation.orderDetailsScreenRoute
import com.bunbeauty.presentation.feature.orderlist.navigation.navigateToOrderListScreen
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
            navController.navigateToOrderListScreen(emptyNavOptions)
        },
    )

    orderListScreenRoute(
        cancelNotification = cancelNotification,
        openOrderDetails = { orderUuid, orderCode ->
            navController.navigateToOrderDetailsScreen(orderUuid, orderCode, emptyNavOptions)
        },
    )

    menuScreenRoute(
        goToCategoriesScreen = {
            navController.navigateToCategoryListScreen(emptyNavOptions)
        },
        goToMenuListScreen = {
            navController.navigateToMenuListScreen(emptyNavOptions)
        },
        goToAdditionGroupListScreen = {
            navController.navigateToAdditionGroupListScreen(emptyNavOptions)
        },
        goToAdditionListScreen = {
            navController.navigateToAdditionListScreen(emptyNavOptions)
        },
    )

    menuListScreenRoute(
        goToCreateMenuProductScreen = {
            navController.navigateToCreateMenuProductScreen(emptyNavOptions)
        },
        goToEditMenuProductScreen = { uuid ->
            navController.navigateToEditMenuProductScreen(
                menuProductUuid = uuid,
                navOptions = emptyNavOptions,
            )
        },
        back = navController::navigateUp
    )

    createMenuProductScreenRoute(
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = navController::navigateUp,
        goToCategoryList = { selectedCategoryList ->
            navController.navigateToSelectCategoryListScreen(
                selectedCategoryList = selectedCategoryList,
                navOptions = emptyNavOptions
            )
        },
        goToCropImage = { imageUri ->
            // navigate to crop image - handled by parent
        },
    )
    editMenuProductScreenRoute(
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = navController::navigateUp,
        goToCategoryList = { selectedCategoryList ->
            navController.navigateToSelectCategoryListScreen(
                selectedCategoryList = selectedCategoryList,
                navOptions = emptyNavOptions
            )
        },
        goToAdditionList = { menuProductUuid ->
            // navigate to addition list - handled by parent
        },
        goToCropImage = { imageUri ->
            // navigate to crop image - handled by parent
        },
    )
    additionGroupListScreenRoute(
        goBack = navController::navigateUp,
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
        goBack = navController::navigateUp,
        goToCreateAdditionScreen = {
            navController.navigateToCreateAdditionScreen(
                navOptions = emptyNavOptions,
            )
        },
        goToEditAdditionScreen = { uuid ->
            navController.navigateToEditAdditionScreen(emptyNavOptions)
        },
    )
    createAdditionScreenRoute(
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = navController::navigateUp,
        goToCropImage = { imageUri ->
            // navigate to crop image - handled by parent
        },
    )
    categoryListScreenRoute(
        goBack = navController::navigateUp,
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
        goBack = navController::navigateUp,
    )
    editCategoryScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = navController::navigateUp,
    )
    createAdditionGroupScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = navController::navigateUp,
    )
    editAdditionGroupScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = navController::navigateUp,
    )
    editDeliveryZoneInfoScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = navController::navigateUp,
    )
    mapDeliveryZoneScreenRoute(
        goBack = navController::navigateUp,
        goToEditDeliveryZoneInfo = { zoneUuid ->
            navController.navigateToEditDeliveryZoneInfoScreen(
                zoneUuid = zoneUuid,
                navOptions = emptyNavOptions,
            )
        },
        onZoneUpdated = { zoneUuid ->
            navController.navigateToEditDeliveryZoneInfoScreen(zoneUuid, emptyNavOptions)
        },
    )
    galleryScreenRoute(
        goBack = navController::navigateUp,
    )
    statisticDetailsScreenRoute(
        goBack = navController::navigateUp,
    )
    selectCategoryListScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = navController::navigateUp,
        onSaveCategoryList = {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(SELECTED_CATEGORY_UUID_LIST, it)
//            savedStateHandle.getStateFlow(
//                key = SELECTED_CATEGORY_UUID_LIST,
//                initialValue = emptyList<String>()
//            )
//                .collect {
//                    onAction(
//                        action =
//                            CreateMenuProduct.Action.SelectCategories(categoryUuidList = it)
//                    )
//                }
            navController.navigateUp()
        }
    )
    cropImageScreenRoute(
        goBack = navController::navigateUp,
    )
    additionGroupForMenuProductListScreenRoute(
        showInfoMessage = showInfoMessage,
        goBack = navController::navigateUp,
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
        goBack = navController::navigateUp,
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
        goBack = navController::navigateUp,
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
        goBack = navController::navigateUp,
        onAdditionGroupSelected = { additionGroupUuid, additionGroupName ->
            // Result handled by calling screen
        },
    )
    selectAdditionListScreenRoute(
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = navController::navigateUp,
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
            // navigate to map - handled by existing navigation
        },
        goToLoginScreen = {
            // TODO CLEAN BACK STACK
            navController.navigateToLoginScreen(emptyNavOptions)
        },
    )
    settingsScreenRoute(
        goBack = navController::navigateUp,
    )
    statisticScreenRoute(
        goBack = navController::navigateUp,
    )

    orderDetailsScreenRoute(
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = navController::navigateUp,
        onCallPhone = { phoneNumber ->
            // Phone call - handled by platform-specific implementation
        },
        onCancellationConfirmed = {
            // Cancellation confirmed - handled by parent
        },
    )

    editAdditionScreenRoute(
        goBack = navController::navigateUp,
        showInfoMessage = showInfoMessage,
    )
}
