package com.bunbeauty.domain.feature.menu.addmenuproduct

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductOldPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductPhotoLinkException
import com.bunbeauty.domain.model.category.SelectableCategory
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class AddMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {

    data class Params(
        val name: String,
        val newPrice: String,
        val oldPrice: String,
        val nutrition: String,
        val utils: String,
        val description: String,
        val comboDescription: String,
        val photoLink: String?,
        val isVisible: Boolean,
        val isRecommended: Boolean,
        val categories: List<SelectableCategory>
    )

    suspend operator fun invoke(params: Params) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        val name = params.name.takeIf { it.isNotBlank() } ?: throw MenuProductNameException()
        val newPrice = params.newPrice.toIntOrNull()
            ?.takeIf { it > 0 }
            ?: throw MenuProductNewPriceException()
        val oldPrice = params.oldPrice.toIntOrNull()
        if (oldPrice != null && oldPrice <= newPrice) {
            throw MenuProductOldPriceException()
        }
        val description = params.description.takeIf { it.isNotBlank() } ?: throw MenuProductDescriptionException()
        val categories = params.categories.takeIf { it.isNotEmpty() } ?: throw MenuProductCategoriesException()
        val photoLink = params.photoLink ?: throw MenuProductPhotoLinkException()

        menuProductRepo.post(
            token = token,
            menuProductPost = MenuProductPost(
                name = name,
                newPrice = newPrice,
                oldPrice = params.oldPrice.toIntOrNull(),
                nutrition = params.nutrition.toIntOrNull(),
                utils = params.utils,
                description = description,
                comboDescription = params.comboDescription,
                photoLink = photoLink,
                barcode = 0,
                isVisible = params.isVisible,
                isRecommended = params.isRecommended,
                categories = categories.map { category ->
                    category.category.uuid
                },
            )
        )
    }
}
