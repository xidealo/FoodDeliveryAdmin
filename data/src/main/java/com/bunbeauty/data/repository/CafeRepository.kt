package com.bunbeauty.data.repository

import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.mapper.cafe.IEntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IServerCafeMapper
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.CafeRepo
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val apiRepo: ApiRepo,
    private val serverCafeMapper: IServerCafeMapper,
    private val entityCafeMapper: IEntityCafeMapper,
    private val cafeDao: CafeDao
) : CafeRepo {

    override val cafeListFlow = cafeDao.getCafeListFlow()
        .flowOn(IO)
        .map { cafeEntityList ->
            cafeEntityList.map { cafeEntity ->
                entityCafeMapper.from(cafeEntity)
            }
        }
        .flowOn(Default)

    override fun getCafeByUuid(uuid: String): Flow<Cafe?> = cafeDao.getCafeByUuid(uuid)
        .flowOn(IO)
        .map { cafeEntity ->
            cafeEntity?.let {
                entityCafeMapper.from(cafeEntity)
            }
        }
        .flowOn(Default)

    override suspend fun getCafeList() = withContext(IO) {
        cafeDao.getCafeList().map { cafeEntity ->
            entityCafeMapper.from(cafeEntity)
        }
    }

    override suspend fun refreshCafeList() = withContext(IO) {
        cafeDao.deleteAll()
        apiRepo.getCafeList().collect { serverCafeList ->
            val cafeEntity = serverCafeList.map { serverCafe ->
                serverCafeMapper.from(serverCafe)
            }

            cafeDao.insertAll(cafeEntity)
        }
    }
}