package com.bunbeauty.domain.feature.menu.editmenuproduct

import android.util.Log
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.domain.feature.menu.common.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.menu.common.photo.UploadPhotoUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductCategoriesUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductDescriptionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNameUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNewPriceUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNutritionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductOldPriceUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.MenuProductNotUpdatedException
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

private const val UPDATE_MENU_PRODUCT_TAG = "UpdateMenuProduct"

class UpdateMenuProductUseCase @Inject constructor(
    private val validateMenuProductNameUseCase: ValidateMenuProductNameUseCase,
    private val validateMenuProductNewPriceUseCase: ValidateMenuProductNewPriceUseCase,
    private val validateMenuProductOldPriceUseCase: ValidateMenuProductOldPriceUseCase,
    private val validateMenuProductNutritionUseCase: ValidateMenuProductNutritionUseCase,
    private val validateMenuProductDescriptionUseCase: ValidateMenuProductDescriptionUseCase,
    private val validateMenuProductCategoriesUseCase: ValidateMenuProductCategoriesUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {

    data class Params(
        val uuid: String,
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
        val photoLink: String?,
        val newImageUri: String?
    )

    suspend operator fun invoke(params: Params) {
        val name = validateMenuProductNameUseCase(name = params.name)
        val newPrice = validateMenuProductNewPriceUseCase(newPrice = params.newPrice)
        val oldPrice = validateMenuProductOldPriceUseCase(
            oldPrice = params.oldPrice,
            newPrice = newPrice
        ) ?: 0
        val nutrition = validateMenuProductNutritionUseCase(
            nutrition = params.nutrition,
            units = params.units
        ) ?: 0
        val description = validateMenuProductDescriptionUseCase(description = params.description)
        val selectableCategories = validateMenuProductCategoriesUseCase(categories = params.selectedCategories)

        if (params.photoLink == null && params.newImageUri == null) {
            throw MenuProductImageException()
        }
        var newPhotoLink: String? = null
        if (params.newImageUri != null) {
            newPhotoLink = uploadPhotoUseCase(imageUri = params.newImageUri).url
        }
        if (params.photoLink != null && params.newImageUri != null) {
            runCatching {
                deletePhotoUseCase(photoLink = params.photoLink)
            }.onFailure {
                Log.e(UPDATE_MENU_PRODUCT_TAG, "Photo deletion failed ${it.message}")
            }
        }

        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        menuProductRepo.updateMenuProduct(
            menuProductUuid = params.uuid,
            updateMenuProduct = UpdateMenuProduct(
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                nutrition = nutrition,
                utils = params.units,
                description = description,
                comboDescription = params.comboDescription.trim(),
                photoLink = newPhotoLink,
                isVisible = params.isVisible,
                isRecommended = params.isRecommended,
                categories = selectableCategories.map { category ->
                    category.category.uuid
                }
            ),
            token = token
        ) ?: throw MenuProductNotUpdatedException()
    }
}
