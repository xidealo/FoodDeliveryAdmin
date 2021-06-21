package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.AddressRepo
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.data.dao.CafeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val cafeDao: CafeDao,
    private val addressRepo: AddressRepo,
    private val apiRepo: ApiRepo
) : CafeRepo {

    override val cafeList = cafeDao.getCafeList()

    override fun getCafeById(id: String): Cafe? {
        return cafeDao.getCafeById(id)
    }

    override fun getCafeByIdFlow(id: String): Flow<Cafe?> {
        return cafeDao.getCafeByIdFlow(id)
    }

    override suspend fun refreshCafeList() {
        cafeDao.deleteAll()

        apiRepo.getCafeList().collect { cafeList ->
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