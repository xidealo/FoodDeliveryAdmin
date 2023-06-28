package com.bunbeauty.domain.use_case

import com.bunbeauty.domain.NotificationService
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val notificationService: NotificationService,
    private val menuProductRepo: MenuProductRepo,
) {

    suspend operator fun invoke() {
        dataStoreRepo.cafeUuid.firstOrNull()?.let { cafeUuid ->
            notificationService.unsubscribeFromNotifications(cafeUuid)
        }
        dataStoreRepo.clearCache()
        menuProductRepo.clearMenuProductList()
    }
}
