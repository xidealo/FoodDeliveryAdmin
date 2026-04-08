package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.shared.feature.additiongrouplist.AdditionGroupListViewModel
import com.bunbeauty.shared.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupViewModel
import com.bunbeauty.shared.feature.additiongrouplist.editadditiongroup.EditAdditionGroupViewModel
import com.bunbeauty.shared.feature.additionlist.AdditionListViewModel
import com.bunbeauty.shared.feature.additionlist.createaddition.CreateAdditionViewModel
import com.bunbeauty.shared.feature.additionlist.editadditionlist.EditAdditionViewModel
import com.bunbeauty.shared.feature.category.CategoryListViewModel
import com.bunbeauty.shared.feature.category.createcategory.CreateCategoryViewModel
import com.bunbeauty.shared.feature.category.editcategory.EditCategoryViewModel
import com.bunbeauty.shared.feature.gallery.GalleryViewModel
import com.bunbeauty.shared.feature.gallery.selectphoto.SelectPhotoViewModel
import com.bunbeauty.shared.feature.login.LoginViewModel
import com.bunbeauty.shared.feature.mapdelivery.MapDeliveryZoneViewModel
import com.bunbeauty.shared.feature.mapdelivery.editinfodeliveryzone.EditDeliveryZoneInfoViewModel
import com.bunbeauty.shared.feature.menulist.MenuListViewModel
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductListViewModel
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateAdditionGroupForMenuProductViewModel
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductViewModel
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListViewModel
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupViewModel
import com.bunbeauty.shared.feature.menulist.categorylist.SelectCategoryListViewModel
import com.bunbeauty.shared.feature.menulist.createmenuproduct.CreateMenuProductViewModel
import com.bunbeauty.shared.feature.menulist.cropimage.CropImageViewModel
import com.bunbeauty.shared.feature.menulist.editmenuproduct.EditMenuProductViewModel
import com.bunbeauty.shared.feature.order.OrderDetailsViewModel
import com.bunbeauty.shared.feature.orderlist.OrderListViewModel
import com.bunbeauty.shared.feature.profile.ProfileViewModel
import com.bunbeauty.shared.feature.settings.SettingsViewModel
import com.bunbeauty.shared.feature.statistic.StatisticViewModel
import com.bunbeauty.shared.feature.statisticdetails.StatisticDetailsViewModel
import com.bunbeauty.shared.viewmodel.main.MainViewModel
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
                additionUuid = it.get(),
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
                additionGroupForMenuUuid = it.get(),
                menuProductUuid = it.get(),
            )
        }

        viewModel {
            CreateAdditionGroupForMenuProductViewModel(
                createEditAdditionGroupWithAdditionsUseCase = get(),
                getAdditionGroupUseCase = get(),
                getAdditionListNameUseCase = get(),
                getAdditionUseCase = get(),
                menuProductUuid = it.get(),
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
                menuProductUuid = it.get(),
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
