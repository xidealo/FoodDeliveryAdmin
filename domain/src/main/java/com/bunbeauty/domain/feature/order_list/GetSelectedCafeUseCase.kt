package com.bunbeauty.domain.feature.order_list

import com.bunbeauty.domain.model.cafe.SelectedCafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetSelectedCafeUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepository: CafeRepo,
) {

    suspend operator fun invoke(): SelectedCafe? {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull()
        val token = dataStoreRepo.token.firstOrNull() ?: return null
        val cityUuid = dataStoreRepo.managerCity.firstOrNull() ?: return null

        val cafeList = cafeRepository.getCafeList(
            token = token,
            cityUuid = cityUuid,
        )
        val cafe = if (cafeUuid == null) {
            cafeList.firstOrNull()
        } else {
            cafeList.find { cafe ->
                cafe.uuid == cafeUuid
            }
        }

        return cafe?.let {
            SelectedCafe(
                uuid = cafe.uuid,
                address = cafe.address,
            )
        }
    }

}
