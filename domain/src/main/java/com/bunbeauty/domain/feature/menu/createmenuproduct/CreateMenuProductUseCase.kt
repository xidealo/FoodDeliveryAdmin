package com.bunbeauty.domain.feature.menu.createmenuproduct

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
import com.bunbeauty.domain.feature.menu.createmenuproduct.exception.MenuProductNotCreatedException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.PhotoRepo
import javax.inject.Inject

class CreateMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val photoRepo: PhotoRepo,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val calculateImageCompressQualityUseCase: CalculateImageCompressQualityUseCase
) {

    data class Params(
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
        val imageUri: String?
    )

    suspend operator fun invoke(params: Params) {
        val name = params.name.takeIf { name ->
            name.isNotBlank()
        } ?: throw MenuProductNameException()
        val newPrice = params.newPrice.toIntOrNull()
            ?.takeIf { it > 0 }
            ?: throw MenuProductNewPriceException()
        val oldPrice = params.oldPrice.toIntOrNull()
        if (oldPrice != null && oldPrice <= newPrice) {
            throw MenuProductOldPriceException()
        }
        val description = params.description.takeIf { it.isNotBlank() } ?: throw MenuProductDescriptionException()
        val selectableCategories = params.selectedCategories.takeIf { it.isNotEmpty() } ?: throw MenuProductCategoriesException()
        val imageUri = params.imageUri ?: throw MenuProductImageException()

        val fileSize = photoRepo.getFileSizeInMb(uri = imageUri)
        val compressQuality = calculateImageCompressQualityUseCase(fileSize = fileSize)
        val photo = photoRepo.uploadPhoto(
            uri = imageUri,
            compressQuality = compressQuality,
            username = getUsernameUseCase()
        ) ?: throw MenuProductUploadingImageException()

        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        menuProductRepo.saveMenuProduct(
            token = token,
            menuProductPost = MenuProductPost(
                name = name,
                newPrice = newPrice,
                oldPrice = params.oldPrice.toIntOrNull(),
                nutrition = params.nutrition.toIntOrNull(),
                utils = params.utils,
                description = description,
                comboDescription = params.comboDescription,
                photoLink = photo.url,
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
