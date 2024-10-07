package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.onSuccess
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.addition.mapAdditionServerToAddition
import com.bunbeauty.data.mapper.addition.mapCreateAdditionToCreateAdditionPostServer
import com.bunbeauty.data.mapper.addition.mapUpdateAdditionServerToPatchAddition
import com.bunbeauty.data.model.server.addition.AdditionServer
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.CreateAddition
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import javax.inject.Inject

class AdditionRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi
) : AdditionRepo {

    private var additionListCache: List<Addition>? = null

    override suspend fun getAdditionList(token: String, refreshing: Boolean): List<Addition> {
        return if (refreshing) {
            fetchAdditionList(token = token)
        } else {
            additionListCache ?: fetchAdditionList(token = token)
        }
    }

    override suspend fun getAddition(additionUuid: String, token: String): Addition? {
        val addition = additionListCache?.find { addition ->
            addition.uuid == additionUuid
        }
        return addition ?: fetchAdditionList(
            token = token
        ).find { foundAddition ->
            foundAddition.uuid == additionUuid
        }
    }

    private suspend fun fetchAdditionList(
        token: String
    ): List<Addition> {
        return when (val result = networkConnector.getAdditionList(token = token)) {
            is ApiResult.Error -> {
                throw result.apiError
            }

            is ApiResult.Success -> {
                val additionList = result.data.results.map(mapAdditionServerToAddition)
                additionListCache = additionList
                additionList
            }
        }
    }

    override suspend fun post(
        token: String,
        createAddition: CreateAddition
    ) {
        when (
            val result = networkConnector.postCreateAddition(
                token = token,
                createAdditionPostServer = createAddition.mapCreateAdditionToCreateAdditionPostServer()
            )
        ) {
            is ApiResult.Success -> {
            }

            is ApiResult.Error -> {
                throw result.apiError
            }
        }
    }

    override suspend fun updateAddition(
        updateAddition: UpdateAddition,
        token: String,
        additionUuid: String
    ) {
        networkConnector.patchAddition(
            additionUuid = additionUuid,
            additionPatchServer = updateAddition.mapUpdateAdditionServerToPatchAddition(),
            token = token
        ).onSuccess { additionServer ->
            updateLocalCache(
                uuid = additionUuid,
                additionServer = additionServer
            )
        }
    }

    private fun updateLocalCache(
        uuid: String,
        additionServer: AdditionServer
    ) {
        additionListCache = additionListCache?.map { addition ->
            if (uuid == addition.uuid) {
                additionServer.mapAdditionServerToAddition()
            } else {
                addition
            }
        }
    }

    override suspend fun clearCache() {
        additionListCache = null
    }
}
