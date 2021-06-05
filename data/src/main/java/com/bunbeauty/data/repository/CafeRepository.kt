package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.mapper.cafe.IEntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IServerCafeMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val apiRepo: ApiRepo,
    private val serverCafeMapper: IServerCafeMapper,
    private val entityCafeMapper: IEntityCafeMapper,
    private val cafeDao: CafeDao
) : CafeRepo {

    override val cafeList = cafeDao.getCafeList().map { cafeEntityList ->
        cafeEntityList.map { cafeEntity ->
            entityCafeMapper.from(cafeEntity)
        }
    }

    override fun getCafeByUuid(uuid: String): Flow<Cafe?> {
        return cafeDao.getCafeByUuid(uuid).map { cafeEntity ->
            cafeEntity?.let {
                entityCafeMapper.from(cafeEntity)
            }
        }
    }

    override suspend fun refreshCafeList() {
        cafeDao.deleteAll()

        apiRepo.getCafeList().collect { serverCafeList ->
            val cafeEntity = serverCafeList.map { serverCafe ->
                serverCafeMapper.from(serverCafe)
            }

            cafeDao.insertAll(cafeEntity)
        }
    }
}