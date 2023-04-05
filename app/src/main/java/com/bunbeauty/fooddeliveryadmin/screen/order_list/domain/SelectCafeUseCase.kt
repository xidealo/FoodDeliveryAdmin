package com.bunbeauty.fooddeliveryadmin.screen.order_list.domain

import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.domain.NotificationService
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SelectCafeUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepository: CafeRepository,
    private val notificationService: NotificationService
) {

    suspend operator fun invoke(cafeUuid: String? = null): SelectedCafe? {
        val savedCafeUuid = dataStoreRepo.cafeUuid.firstOrNull()

        if (cafeUuid != null && savedCafeUuid != null) {
            notificationService.unsubscribeFromNotifications(savedCafeUuid)
        }
        return if (cafeUuid == null) {
            if (savedCafeUuid == null) {
                dataStoreRepo.managerCity.firstOrNull()?.let { cityUuid ->
                    cafeRepository.getCafeListByCityUuid(cityUuid).firstOrNull()
                }
            } else {
                cafeRepository.getCafeByUuid(savedCafeUuid)
            }
        } else {
            cafeRepository.getCafeByUuid(cafeUuid)
        }?.let { cafe ->
            dataStoreRepo.saveCafeUuid(cafe.uuid)
            notificationService.subscribeOnNotifications(cafe.uuid)
            SelectedCafe(
                uuid = cafe.uuid,
                address = cafe.address
            )
        }
    }
}
