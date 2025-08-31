package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.editcafe.GetNonWorkingDayYearRangeUseCase
import com.bunbeauty.domain.feature.menu.common.category.CreateCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.category.EditCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.category.GetCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.category.GetCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.category.GetSelectableCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.category.SaveCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductCategoriesUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductDescriptionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNameUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNewPriceUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNutritionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductOldPriceUseCase
import com.bunbeauty.domain.feature.menu.createmenuproduct.CreateMenuProductUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.GetMenuProductUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.UpdateMenuProductUseCase
import com.bunbeauty.domain.feature.order.usecase.LoadOrderDetailsUseCase
import com.bunbeauty.domain.feature.order.usecase.UpdateOrderStatusUseCase
import com.bunbeauty.domain.feature.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.usecase.GetSeparatedMenuProductListUseCase
import com.bunbeauty.domain.usecase.GetStatisticUseCase
import com.bunbeauty.domain.usecase.LogoutUseCase
import com.bunbeauty.domain.usecase.UpdateVisibleMenuProductUseCase
import com.bunbeauty.domain.util.GetNewUuidUseCase
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.domain.util.product.ProductUtil
import org.koin.dsl.module

fun useCaseModule() = module {
    factory {
        GetCafeUseCase(
            dataStoreRepo = get(),
            cafeRepository = get()
        )
    }

    factory {
        GetNonWorkingDayYearRangeUseCase()
    }

    factory {
        GetCategoryListUseCase(
            dataStoreRepo = get(),
            categoryRepo = get()
        )
    }

    factory {
        SaveCategoryListUseCase(
            dataStoreRepo = get(),
            categoryRepo = get()
        )
    }

    factory {
        GetSelectableCategoryListUseCase(
            getCategoryListUseCase = get()
        )
    }

    factory {
        CreateCategoryUseCase(
            dataStoreRepo = get(),
            categoryRepo = get()
        )
    }

    factory {
        GetCategoryUseCase(
            dataStoreRepo = get(),
            categoryRepo = get()
        )
    }

    factory {
        EditCategoryUseCase(
            dataStoreRepo = get(),
            categoryRepo = get()
        )
    }

    factory {
        DeletePhotoUseCase(
            photoRepo = get()
        )
    }

    factory {
        UploadPhotoUseCase(
            photoRepo = get(),
            getUsernameUseCase = get()
        )
    }

    factory {
        ValidateMenuProductCategoriesUseCase()
    }

    factory {
        ValidateMenuProductDescriptionUseCase()
    }

    factory {
        ValidateMenuProductNameUseCase()
    }

    factory {
        ValidateMenuProductNewPriceUseCase()
    }

    factory {
        ValidateMenuProductNutritionUseCase()
    }

    factory {
        ValidateMenuProductOldPriceUseCase()
    }

    factory {
        CreateMenuProductUseCase(
            validateMenuProductNameUseCase = get(),
            validateMenuProductNutritionUseCase = get(),
            validateMenuProductDescriptionUseCase = get(),
            validateMenuProductCategoriesUseCase = get(),
            validateMenuProductNewPriceUseCase = get(),
            validateMenuProductOldPriceUseCase = get(),
            dataStoreRepo = get(),
            menuProductRepo = get(),
            uploadPhotoUseCase = get()
        )
    }

    factory {
        UpdateMenuProductUseCase(
            validateMenuProductNameUseCase = get(),
            validateMenuProductNutritionUseCase = get(),
            validateMenuProductDescriptionUseCase = get(),
            validateMenuProductCategoriesUseCase = get(),
            validateMenuProductNewPriceUseCase = get(),
            validateMenuProductOldPriceUseCase = get(),
            dataStoreRepo = get(),
            menuProductRepo = get(),
            uploadPhotoUseCase = get(),
            deletePhotoUseCase = get()
        )
    }

    factory {
        GetMenuProductUseCase(
            dataStoreRepo = get(),
            menuProductRepo = get()
        )
    }

    factory {
        LoadOrderDetailsUseCase(
            dataStoreRepo = get(),
            orderRepo = get()
        )
    }

    factory {
        UpdateOrderStatusUseCase(
            dataStoreRepo = get(),
            orderRepo = get()
        )
    }

    factory {
        GetSeparatedMenuProductListUseCase(
            dataStoreRepo = get(),
            menuProductRepo = get()
        )
    }

    factory {
        GetStatisticUseCase(
            dataStoreRepo = get(),
            statisticRepo = get()
        )
    }

    factory {
        LogoutUseCase(
            dataStoreRepo = get(),
            additionRepo = get(),
            additionGroupRepo = get(),
            cafeRepo = get(),
            userAuthorizationRepo = get(),
            nonWorkingDayRepo = get(),
            cityRepo = get(),
            menuProductRepo = get(),
            orderRepo = get(),
            photoRepo = get(),
            categoryRepo = get()
        )
    }

    factory {
        UpdateVisibleMenuProductUseCase(
            dataStoreRepo = get(),
            menuProductRepo = get()
        )
    }

    factory {
        GetNewUuidUseCase()
    }

    factory {
        DateTimeUtil()
    }

    factory {
        ProductUtil()
    }
}
