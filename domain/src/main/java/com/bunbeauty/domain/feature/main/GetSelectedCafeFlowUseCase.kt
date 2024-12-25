package com.bunbeauty.domain.feature.main

import com.bunbeauty.domain.model.cafe.SelectedCafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetSelectedCafeFlowUseCase (
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<SelectedCafe?> {
        return dataStoreRepo.managerCity.flatMapLatest { cityUuid ->
            val cafeList = cafeRepo.getCafeList(cityUuid = cityUuid)

            dataStoreRepo.cafeUuid.map { cafeUuid ->
                val cafe = if (cafeUuid == null) {
                    cafeList.firstOrNull()
                } else {
                    cafeList.find { cafe ->
                        cafe.uuid == cafeUuid
                    }
                }

                cafe?.let {
                    SelectedCafe(
                        uuid = cafe.uuid,
                        address = cafe.address
                    )
                }
            }
        }
    }
}
