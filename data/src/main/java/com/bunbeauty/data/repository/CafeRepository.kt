package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.mapper.cafe.IEntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IServerCafeMapper
import com.bunbeauty.domain.model.cafe.Cafe
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
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
) {

    val cafeListFlow = cafeDao.getCafeListFlow()
        .flowOn(IO)
        .map { cafeEntityList ->
            cafeEntityList.map { cafeEntity ->
                entityCafeMapper.from(cafeEntity)
            }
        }
        .flowOn(Default)

    fun observeCafeByUuid(uuid: String): Flow<Cafe?> = cafeDao.observeCafeByUuid(uuid)
        .flowOn(IO)
        .map { cafeEntity ->
            cafeEntity?.let {
                entityCafeMapper.from(cafeEntity)
            }
        }
        .flowOn(Default)

    suspend fun getCafeByUuid(uuid: String): Cafe? =
        cafeDao.getCafeByUuid(uuid)?.let { cafeEntity ->
            entityCafeMapper.from(cafeEntity)
        }

    suspend fun getCafeList() = withContext(IO) {
        cafeDao.getCafeList().map { cafeEntity ->
            entityCafeMapper.from(cafeEntity)
        }
    }

    suspend fun refreshCafeList(token: String, cityUuid: String) {
        when (val result = networkConnector.getCafeList(token, cityUuid)) {
            is ApiResult.Success -> {
                result.data.let { listServer ->
                    cafeDao.insertAll(
                        listServer.results.map { serverCafe ->
                            serverCafeMapper.from(serverCafe)
                        })
                }
            }
            is ApiResult.Error -> {
                Log.e(
                    "testTag",
                    "refreshCafeList ${result.apiError.message} ${result.apiError.code}"
                )
            }
        }
    }
}