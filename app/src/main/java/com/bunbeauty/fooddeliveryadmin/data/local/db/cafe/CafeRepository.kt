package com.bunbeauty.fooddeliveryadmin.data.local.db.cafe

import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.local.db.address.AddressRepo
import com.bunbeauty.fooddeliveryadmin.data.model.Cafe
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class CafeRepository @Inject constructor(
        private val cafeDao: CafeDao,
        private val apiRepository: IApiRepository,
        private val addressRepo: AddressRepo
) : CafeRepo {

    override val cafeListLiveData = cafeDao.getCafeListLiveData()

    override suspend fun refreshCafeList() {
        cafeDao.deleteAll()

        apiRepository.getCafeList().collect { cafeList ->
            for (cafe in cafeList) {
                saveCafe(cafe)
            }
        }
    }

    suspend fun saveCafe(cafe: Cafe) {
        cafeDao.insert(cafe.cafeEntity)

        cafe.address.cafeId = cafe.cafeEntity.id
        addressRepo.insert(cafe.address)
    }
}