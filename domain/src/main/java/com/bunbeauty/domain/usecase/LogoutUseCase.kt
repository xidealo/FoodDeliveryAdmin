package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.NotificationService
import com.bunbeauty.domain.feature.orderlist.GetSelectedCafeUseCase
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import com.bunbeauty.domain.repo.PhotoRepo
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val notificationService: NotificationService,
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo,
    private val cityRepo: CityRepo,
    private val menuProductRepo: MenuProductRepo,
    private val nonWorkingDayRepo: NonWorkingDayRepo,
    private val photoRepo: PhotoRepo
) {

    suspend operator fun invoke() {
        getSelectedCafe()?.let { selectedCafe ->
            notificationService.unsubscribeFromNotifications(selectedCafe.uuid)
        }
        dataStoreRepo.clearCache()
        cafeRepo.clearCache()
        cityRepo.clearCache()
        menuProductRepo.clearCache()
        nonWorkingDayRepo.clearCache()
    }
}
