package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class UpdateMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(menuProduct: MenuProduct) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        if (menuProduct.name.isEmpty()) throw MenuProductNameException()
        if (menuProduct.newPrice == 0) throw MenuProductNewPriceException()
        if (menuProduct.description.isEmpty()) throw MenuProductDescriptionException()

        menuProductRepo.updateMenuProduct(menuProduct = menuProduct, token = token)
    }
}