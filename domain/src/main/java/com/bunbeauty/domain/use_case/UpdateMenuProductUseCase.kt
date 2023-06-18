package com.bunbeauty.domain.use_case

import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class UpdateMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo
) {

    suspend operator fun invoke(menuProduct: MenuProduct) {
        menuProductRepo.updateMenuProduct(menuProduct)
    }
}