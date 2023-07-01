package com.bunbeauty.domain.use_case

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class UpdateMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(menuProduct: MenuProduct) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        menuProductRepo.updateMenuProduct(menuProduct = menuProduct, token = token)
    }
}