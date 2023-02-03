package com.bunbeauty.fooddeliveryadmin.screen.order_list.domain

import com.bunbeauty.data.repository.CafeNotificationRepository
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SelectCafeUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepository: CafeRepository,
    private val cafeNotificationRepository: CafeNotificationRepository
) {

    suspend operator fun invoke(cafeUuid: String? = null): SelectedCafe? {
        val savedCafeUuid = dataStoreRepo.cafeUuid.firstOrNull()

        if (cafeUuid != null && savedCafeUuid != null) {
            cafeNotificationRepository.unsubscribeFromCafeNotification(savedCafeUuid)
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
            cafeNotificationRepository.subscribeOnCafeNotification(cafe.uuid)
            SelectedCafe(
                uuid = cafe.uuid,
                address = cafe.address
            )
        }
    }
}
