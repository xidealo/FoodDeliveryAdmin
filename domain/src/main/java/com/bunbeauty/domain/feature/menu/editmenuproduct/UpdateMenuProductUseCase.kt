package com.bunbeauty.domain.feature.menu.editmenuproduct

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.CalculateImageCompressQualityUseCase
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.MenuProductNotUpdatedException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.PhotoRepo
import javax.inject.Inject

class UpdateMenuProductUseCase @Inject constructor(
    private val photoRepo: PhotoRepo,
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val calculateImageCompressQualityUseCase: CalculateImageCompressQualityUseCase
) {

    data class Params(
        val uuid: String,
        val name: String,
        val newPrice: String,
        val oldPrice: String,
        val nutrition: String,
        val utils: String,
        val description: String,
        val comboDescription: String,
        val selectedCategories: List<SelectableCategory>,
        val isVisible: Boolean,
        val isRecommended: Boolean,
        val photoLink: String?,
        val imageUri: String?
    )

    suspend operator fun invoke(params: Params) {
        val name = validateName(name = params.name)
        val newPrice = validateNewPrice(newPrice = params.newPrice)
        val oldPrice = validateOldPrice(
            oldPrice = params.oldPrice,
            newPrice = newPrice
        )
        val description = validateDescription(description = params.description)
        val selectableCategories = validateCategories(categories = params.selectedCategories)

        var updatedPhotoLink: String? = null
        if (params.photoLink == null) {
            val imageUri = params.imageUri ?: throw MenuProductImageException()
            val fileSize = photoRepo.getFileSizeInMb(uri = imageUri)
            val compressQuality = calculateImageCompressQualityUseCase(fileSize = fileSize)
            val photo = photoRepo.uploadPhoto(
                uri = imageUri,
                compressQuality = compressQuality,
                username = getUsernameUseCase()
            ) ?: throw MenuProductUploadingImageException()

            updatedPhotoLink = photo.url
        }

        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        menuProductRepo.updateMenuProduct(
            menuProductUuid = params.uuid,
            updateMenuProduct = UpdateMenuProduct(
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                nutrition = params.nutrition.toIntOrNull(),
                utils = params.utils,
                description = description,
                comboDescription = params.comboDescription,
                photoLink = updatedPhotoLink,
                isVisible = params.isVisible,
                isRecommended = params.isRecommended,
                categories = selectableCategories.map { category ->
                    category.category.uuid
                }
            ),
            token = token
        ) ?: throw MenuProductNotUpdatedException()
    }

    private fun validateName(name: String): String {
        return name.takeIf { value ->
            value.isNotBlank()
        } ?: throw MenuProductNameException()
    }

    private fun validateNewPrice(newPrice: String): Int {
        return newPrice.toIntOrNull()
            ?.takeIf { value ->
                value > 0
            } ?: throw MenuProductNewPriceException()
    }

    private fun validateOldPrice(oldPrice: String, newPrice: Int): Int? {
        if (oldPrice.isBlank()) {
            return 0
        }

        val oldPriceInt = oldPrice.toIntOrNull()
        if (oldPriceInt != null && oldPriceInt <= newPrice) {
            throw MenuProductOldPriceException()
        }

        return oldPriceInt
    }

    private fun validateDescription(description: String): String {
        return description.takeIf { value ->
            value.isNotBlank()
        } ?: throw MenuProductDescriptionException()
    }

    private fun validateCategories(categories: List<SelectableCategory>): List<SelectableCategory> {
        return categories.takeIf {
            categories.isNotEmpty()
        } ?: throw MenuProductCategoriesException()
    }

}
