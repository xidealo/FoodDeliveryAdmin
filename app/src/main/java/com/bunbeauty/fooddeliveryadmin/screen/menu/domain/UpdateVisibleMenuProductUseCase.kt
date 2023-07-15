package com.bunbeauty.fooddeliveryadmin.screen.menu.domain

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateVisibleMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(uuid: String, isVisible: Boolean) {

        menuProductRepo.updateVisibleMenuProductUseCase(
            uuid = uuid,
            isVisible = isVisible,
            token = dataStoreRepo.token.first()
        )
    }
}
