package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.onSuccess
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.addition.mapAdditionServerToAddition
import com.bunbeauty.data.mapper.addition.mapCreateAdditionToAdditionPostServer
import com.bunbeauty.data.mapper.addition.mapUpdateAdditionServerToPatchAddition
import com.bunbeauty.data.model.server.addition.AdditionServer
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.CreateAdditionModel
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo

class AdditionRepository(
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

    override suspend fun createAddition(
        token: String,
        createAdditionModel: CreateAdditionModel
    ) {
        networkConnector.postAddition(
            additionPostServer = createAdditionModel.mapCreateAdditionToAdditionPostServer(),
            token = token
        ).onSuccess { additionServer ->
            addToLocalCache(
                uuid = additionServer.uuid,
                additionServer = additionServer
            )
        }
    }

    override fun clearCache() {
        additionListCache = null
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

    private fun addToLocalCache(
        uuid: String,
        additionServer: AdditionServer
    ) {
        additionListCache = additionListCache?.toMutableList()?.apply {
            add(additionServer.mapAdditionServerToAddition())
        }
    }
}
