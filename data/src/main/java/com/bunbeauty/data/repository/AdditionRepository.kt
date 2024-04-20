package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.addition.mapAdditionServerToAddition
import com.bunbeauty.data.mapper.addition.mapUpdateAdditionServerToPatchAddition
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import javax.inject.Inject

class AdditionRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
) : AdditionRepo {

    override suspend fun getAdditionList(token: String, takeRemote: Boolean): List<Addition> {
        return when (val result = networkConnector.getAdditionList(token = token)) {
            is ApiResult.Error -> {
                throw result.apiError
            }

            is ApiResult.Success -> {
                result.data.results.map(mapAdditionServerToAddition)
            }
        }
    }

    override suspend fun getAddition(additionUuid: String): Addition? {
        TODO("Not yet implemented")
    }

    override suspend fun updateAddition(
        updateAddition: UpdateAddition,
        token: String,
        additionUuid: String,
    ) {
        networkConnector.patchAddition(
            additionUuid = additionUuid,
            additionPatchServer = updateAddition.mapUpdateAdditionServerToPatchAddition(),
            token = token
        )
    }

    override suspend fun clearCache() {
        TODO("Not yet implemented")
    }
}