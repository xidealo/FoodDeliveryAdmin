package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.presentation.feature.additiongrouplist.AdditionGroupListViewModel
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupViewModel
import com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup.EditAdditionGroupViewModel
import com.bunbeauty.presentation.feature.additionlist.AdditionListViewModel
import com.bunbeauty.presentation.feature.additionlist.createaddition.CreateAdditionViewModel
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAdditionViewModel
import com.bunbeauty.presentation.feature.category.CategoryListViewModel
import com.bunbeauty.presentation.feature.category.createcategory.CreateCategoryViewModel
import com.bunbeauty.presentation.feature.category.editcategory.EditCategoryViewModel
import com.bunbeauty.presentation.feature.gallery.GalleryViewModel
import com.bunbeauty.presentation.feature.gallery.selectphoto.SelectPhotoViewModel
import com.bunbeauty.presentation.feature.login.LoginViewModel
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryZoneViewModel
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditDeliveryZoneInfoViewModel
import com.bunbeauty.presentation.feature.menulist.MenuListViewModel
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductListViewModel
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateAdditionGroupForMenuProductViewModel
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductViewModel
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListViewModel
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupViewModel
import com.bunbeauty.presentation.feature.menulist.categorylist.SelectCategoryListViewModel
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.CreateMenuProductViewModel
import com.bunbeauty.presentation.feature.menulist.cropimage.CropImageViewModel
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProductViewModel
import com.bunbeauty.presentation.feature.order.OrderDetailsViewModel
import com.bunbeauty.presentation.feature.orderlist.OrderListViewModel
import com.bunbeauty.presentation.feature.profile.ProfileViewModel
import com.bunbeauty.presentation.feature.settings.SettingsViewModel
import com.bunbeauty.presentation.feature.statistic.StatisticViewModel
import com.bunbeauty.presentation.feature.statisticdetails.StatisticDetailsViewModel
import com.bunbeauty.presentation.viewmodel.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule() =
    module {
        viewModel {
            AdditionGroupListViewModel(
                getSeparatedAdditionGroupListUseCase = get(),
                updateVisibleAdditionGroupListUseCase = get(),
            )
        }

        viewModel {
            AdditionListViewModel(
                getSeparatedAdditionListUseCase = get(),
                updateVisibleAdditionUseCase = get(),
            )
        }

        viewModel {
            EditAdditionViewModel(
                getAdditionUseCase = get(),
                updateAdditionUseCase = get(),
                additionUuid = it.get()
            )
        }
        viewModel {
            CreateAdditionViewModel(
                createAdditionUseCase = get(),
            )
        }

        viewModel {
            CreateAdditionGroupViewModel(
                createAdditionGroupUseCase = get(),
            )
        }

        viewModel {
            GalleryViewModel(
                fetchPhotoListUseCase = get(),
                getPhotoListUseCase = get(),
            )
        }

        viewModel {
            SelectPhotoViewModel(
                savedStateHandle = get(),
            )
        }

        viewModel {
            MenuListViewModel(
                getSeparatedMenuProductListUseCase = get(),
                updateVisibleMenuProductUseCase = get(),
            )
        }

        viewModel {
            SelectCategoryListViewModel(
                getSelectableCategoryListUseCase = get(),
            )
        }

        viewModel {
            CreateMenuProductViewModel(
                getSelectableCategoryListUseCase = get(),
                createMenuProductUseCase = get(),
            )
        }

        viewModel {
            CreateCategoryViewModel(
                createCategoryUseCase = get(),
            )
        }

        viewModel {
            AdditionGroupForMenuProductListViewModel(
                getAdditionGroupListFromMenuProductUseCase = get(),
                getAdditionListNameUseCase = get(),
                saveAdditionGroupForMenuProductListUseCase = get(),
            )
        }

        viewModel {
            SelectAdditionGroupViewModel(
                getSeparatedSelectableAdditionGroupListUseCase = get(),
            )
        }

        viewModel {
            EditAdditionGroupForMenuProductViewModel(
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase = get(),
                getAdditionListNameUseCase = get(),
                getAdditionGroupUseCase = get(),
                getAdditionUseCase = get(),
                saveEditAdditionGroupWithAdditionsUseCase = get(),
            )
        }

        viewModel {
            CreateAdditionGroupForMenuProductViewModel(
                createEditAdditionGroupWithAdditionsUseCase = get(),
                getAdditionGroupUseCase = get(),
                getAdditionListNameUseCase = get(),
                getAdditionUseCase = get(),
                menuProductUuid = it.get()
            )
        }

        viewModel {
            SelectAdditionListViewModel(
                getSelectedAdditionListUseCase = get(),
            )
        }

        viewModel {
            EditCategoryViewModel(
                savedStateHandle = get(),
                getCategoryUseCase = get(),
                saveEditCategoryUseCase = get(),
            )
        }

        viewModel {
            CropImageViewModel()
        }

        viewModel {
            EditMenuProductViewModel(
                getSelectableCategoryListUseCase = get(),
                getMenuProductUseCase = get(),
                updateMenuProductUseCase = get(),
                menuProductUuid = it.get()
            )
        }

        viewModel {
            OrderDetailsViewModel(
                loadOrderDetails = get(),
                updateOrderStatus = get(),
            )
        }

        viewModel {
            OrderListViewModel(
                getCafeUseCase = get(),
                getOrderListFlow = get(),
                getOrderErrorFlow = get(),
            )
        }

        viewModel {
            ProfileViewModel(
                getUsernameUseCase = get(),
                isOrderAvailableUseCase = get(),
                logoutUseCase = get(),
            )
        }

        viewModel {
            SettingsViewModel(
                getIsUnlimitedNotification = get(),
                updateIsUnlimitedNotification = get(),
                getTypeWorkUseCase = get(),
                updateTypeWorkUseCase = get(),
                updateWorkCafeUseCase = get(),
            )
        }

        viewModel {
            CategoryListViewModel(
                getCategoryListUseCase = get(),
                saveCategoryListUseCase = get(),
            )
        }

        viewModel {
            StatisticViewModel(
                getStatisticUseCase = get(),
                getCafeUseCase = get(),
            )
        }

        viewModel {
            StatisticDetailsViewModel()
        }

        viewModel {
            MainViewModel(
                getIsNonWorkingDayFlow = get(),
            )
        }

        viewModel {
            LoginViewModel(
                checkAuthorizationUseCase = get(),
                loginUseCase = get(),
            )
        }

        viewModel {
            EditAdditionGroupViewModel(
                savedStateHandle = get(),
                getAdditionGroupUseCase = get(),
                saveEditAdditionGroupUseCase = get(),
            )
        }

        viewModel {
            MapDeliveryZoneViewModel(
                getDeliveryZoneUseCase = get(),
                getCafeUseCase = get(),
                getFullDeliveryZonePointListUseCase = get(),
                getZoneUseCase = get(),
            )
        }

        viewModel {
            EditDeliveryZoneInfoViewModel(
                savedStateHandle = get(),
                getZoneUseCase = get(),
                saveInfoZoneUseCase = get(),
            )
        }
    }
