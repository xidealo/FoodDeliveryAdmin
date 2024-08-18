package com.bunbeauty.domain.usecase

import android.util.Log
import com.bunbeauty.domain.exception.updateproduct.NotFoundMenuProductException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class GetMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo
) {
    suspend operator fun invoke(menuProductUuid: String): MenuProduct {
        return menuProductRepo.getMenuProduct(menuProductUuid = menuProductUuid)
            ?: throw NotFoundMenuProductException()
    }
}
