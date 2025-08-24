package com.bunbeauty.domain.feature.menu.createmenuproduct

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductCategoriesUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductDescriptionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNameUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNewPriceUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNutritionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductOldPriceUseCase
import com.bunbeauty.domain.feature.menu.createmenuproduct.exception.MenuProductNotCreatedException
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo

class CreateMenuProductUseCase(
    private val validateMenuProductNameUseCase: ValidateMenuProductNameUseCase,
    private val validateMenuProductNewPriceUseCase: ValidateMenuProductNewPriceUseCase,
    private val validateMenuProductOldPriceUseCase: ValidateMenuProductOldPriceUseCase,
    private val validateMenuProductNutritionUseCase: ValidateMenuProductNutritionUseCase,
    private val validateMenuProductDescriptionUseCase: ValidateMenuProductDescriptionUseCase,
    private val validateMenuProductCategoriesUseCase: ValidateMenuProductCategoriesUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    data class Params(
        val name: String,
        val newPrice: String,
        val oldPrice: String,
        val nutrition: String,
        val units: String,
        val description: String,
        val comboDescription: String,
        val selectedCategories: List<SelectableCategory>,
        val isVisible: Boolean,
        val isRecommended: Boolean,
        val newImageUri: String?
    )

    suspend operator fun invoke(params: Params) {
        val name = validateMenuProductNameUseCase(name = params.name)
        val newPrice = validateMenuProductNewPriceUseCase(newPrice = params.newPrice)
        val oldPrice = validateMenuProductOldPriceUseCase(
            oldPrice = params.oldPrice,
            newPrice = newPrice
        )
        val nutrition = validateMenuProductNutritionUseCase(
            nutrition = params.nutrition,
            units = params.units
        )
        val description = validateMenuProductDescriptionUseCase(description = params.description)
        val selectableCategories = validateMenuProductCategoriesUseCase(categories = params.selectedCategories)
        val newImageUri = params.newImageUri ?: throw MenuProductImageException()
        val photoLink = uploadPhotoUseCase(imageUri = newImageUri).url

        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        menuProductRepo.saveMenuProduct(
            token = token,
            menuProductPost = MenuProductPost(
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                nutrition = nutrition,
                utils = params.units,
                description = description,
                comboDescription = params.comboDescription.trim(),
                photoLink = photoLink,
                barcode = 0,
                isVisible = params.isVisible,
                isRecommended = params.isRecommended,
                categories = selectableCategories.map { category ->
                    category.category.uuid
                }
            )
        ) ?: throw MenuProductNotCreatedException()
    }
}
