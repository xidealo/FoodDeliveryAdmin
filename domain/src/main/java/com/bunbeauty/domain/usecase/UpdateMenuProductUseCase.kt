package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class UpdateMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(
        menuProductUuid: String,
        updateMenuProduct: UpdateMenuProduct,
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        if (updateMenuProduct.name.isNullOrBlank())
            throw MenuProductNameException()
        if (updateMenuProduct.newPrice == null || updateMenuProduct.newPrice == 0)
            throw MenuProductNewPriceException()
        if (updateMenuProduct.description.isNullOrBlank())
            throw MenuProductDescriptionException()

        val processedUpdateMenuProduct = UpdateMenuProduct(
            name = updateMenuProduct.name,
            newPrice = updateMenuProduct.newPrice,
            oldPrice = updateMenuProduct.oldPrice ?: 0,
            utils = if (updateMenuProduct.nutrition == null || updateMenuProduct.nutrition == 0) {
                ""
            } else {
                updateMenuProduct.utils
            },
            nutrition = if (updateMenuProduct.utils.isNullOrBlank() || updateMenuProduct.nutrition == null) {
                0
            } else {
                updateMenuProduct.nutrition
            },
            description = updateMenuProduct.description,
            comboDescription = updateMenuProduct.comboDescription,
            photoLink = updateMenuProduct.photoLink,
            isVisible = updateMenuProduct.isVisible,
            categoryUuids = updateMenuProduct.categoryUuids,
        )

        menuProductRepo.updateMenuProduct(
            menuProductUuid = menuProductUuid,
            updateMenuProduct = processedUpdateMenuProduct,
            token = token
        )
    }
}