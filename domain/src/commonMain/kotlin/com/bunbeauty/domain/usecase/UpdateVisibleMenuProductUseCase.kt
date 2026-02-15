package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.MenuProductNotUpdatedException
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo

class UpdateVisibleMenuProductUseCase(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(
        menuProductUuid: String,
        isVisible: Boolean,
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        menuProductRepo.updateMenuProduct(
            menuProductUuid = menuProductUuid,
            updateMenuProduct = UpdateMenuProduct(isVisible = isVisible),
            token = token,
        ) ?: throw MenuProductNotUpdatedException()
    }
}
