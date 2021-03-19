package com.bunbeauty.domain.repository.cafe

import com.bunbeauty.domain.repository.address.AddressRepo
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val cafeDao: CafeDao,
    private val addressRepo: AddressRepo,
    private val apiRepository: IApiRepository
) : CafeRepo {

    override val cafeListLiveData = cafeDao.getCafeListLiveData()

    override suspend fun refreshCafeList() {
        cafeDao.deleteAll()

        apiRepository.getCafeList().collect { cafeList ->
            for (cafe in cafeList) {
                cafeDao.insert(cafe.cafeEntity)

                cafe.address?.let { address ->
                    address.cafeId = cafe.cafeEntity.id
                    addressRepo.insert(address)
                }
            }
        }
    }
}