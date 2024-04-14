package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.addition.mapAdditionServerToAddition
import com.bunbeauty.data.mapper.additiongroup.mapAdditionGroupServerToAdditionGroup
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.AdditionRepo
import javax.inject.Inject

class AdditionGroupRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
) : AdditionGroupRepo {

    override suspend fun getAdditionGroupList(token: String, takeRemote: Boolean): List<AdditionGroup> {
        return when(val result = networkConnector.getAdditionGroupList(token = token)){
            is ApiResult.Error -> {
                throw result.apiError
            }
            is ApiResult.Success -> {
                result.data.results.map(mapAdditionGroupServerToAdditionGroup)
            }
        }
    }

    override suspend fun getAdditionGroup(additionUuid: String): AdditionGroup? {
        TODO("Not yet implemented")
    }

    override suspend fun updateAdditionGroup(token: String, addition: AdditionGroup) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCache() {
        TODO("Not yet implemented")
    }
}