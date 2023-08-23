package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.NotificationService
import com.bunbeauty.domain.feature.orderlist.GetSelectedCafeUseCase
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val notificationService: NotificationService,
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo,
    private val menuProductRepo: MenuProductRepo,
) {

    suspend operator fun invoke() {
        getSelectedCafe()?.let { selectedCafe ->
            notificationService.unsubscribeFromNotifications(selectedCafe.uuid)
        }
        dataStoreRepo.clearCache()
        cafeRepo.clearCache()
        menuProductRepo.clearCache()
    }

}