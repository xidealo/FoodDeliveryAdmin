package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.mapper.cafe.IEntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IServerCafeMapper
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.domain.repo.CafeRepo
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
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

    override suspend fun refreshCafeList() {
        //cafeDao.deleteAll()

        when (val result = networkConnector.getCafeList()) {
            is ApiResult.Success -> {
                result.data?.let { listServer ->
                    cafeDao.insertAll(
                        listServer.results.map { serverCafe ->
                            serverCafeMapper.from(serverCafe)
                        })
                }
            }
            is ApiResult.Error -> {
                delay(Constants.RELOAD_DELAY)
                refreshCafeList()
                //reload data
            }
        }
    }
}