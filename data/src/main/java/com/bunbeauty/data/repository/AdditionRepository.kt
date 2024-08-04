package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.onSuccess
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.addition.mapAdditionServerToAddition
import com.bunbeauty.data.mapper.addition.mapUpdateAdditionServerToPatchAddition
import com.bunbeauty.domain.model.addition.Addition
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
        val additive = additionListCache?.find { addition ->
            addition.uuid == additionUuid
        }
        return additions ?: fetchAdditionList(
            token = token
        ).find { addition -> addition.uuid == additionUuid }
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
        ).onSuccess {
            updateLocalCache(
                uuid = additionUuid,
                updateAddition = updateAddition
            )
        }
    }

    private fun updateLocalCache(
        uuid: String,
        updateAddition: UpdateAddition
    ) {
        additionListCache = additionListCache?.map { addition: Addition ->
            if (uuid == addition.uuid) {
                addition.copy(
                    name = updateAddition.name ?: addition.name,
                    priority = updateAddition.priority ?: addition.priority,
                    fullName = updateAddition.fullName ?: addition.fullName,
                    price = updateAddition.price ?: addition.price,
                    photoLink = updateAddition.photoLink ?: addition.photoLink,
                    isVisible = updateAddition.isVisible ?: addition.isVisible
                )
            } else {
                addition
            }
        }
    }

    override suspend fun clearCache() {
        additionListCache = null
    }
}
