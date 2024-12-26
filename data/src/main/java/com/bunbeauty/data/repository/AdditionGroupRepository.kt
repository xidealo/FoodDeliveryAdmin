package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.onSuccess
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.addition.mapAdditionGroupServerToAddition
import com.bunbeauty.data.mapper.addition.mapUpdateAdditionGroupServerToPatchAdditionGroup
import com.bunbeauty.data.mapper.additiongroup.mapAdditionGroupServerToAdditionGroup
import com.bunbeauty.data.model.server.additiongroup.AdditionGroupServer
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo

class AdditionGroupRepository(
    private val networkConnector: FoodDeliveryApi
) : AdditionGroupRepo {

    private var additionGroupListCache: List<AdditionGroup>? = null

    override suspend fun getAdditionGroupList(
        token: String,
        refreshing: Boolean
    ): List<AdditionGroup> {
        return if (refreshing) {
            fetchAdditionGroupList(token = token)
        } else {
            additionGroupListCache ?: fetchAdditionGroupList(token = token)
        }
    }

    private suspend fun fetchAdditionGroupList(token: String): List<AdditionGroup> {
        return when (val result = networkConnector.getAdditionGroupList(token = token)) {
            is ApiResult.Error -> {
                throw result.apiError
            }

            is ApiResult.Success -> {
                result.data.results.map(mapAdditionGroupServerToAdditionGroup)
                    .also { additionGroups ->
                        additionGroupListCache = additionGroups
                    }
            }
        }
    }

    override suspend fun getAdditionGroup(additionUuid: String): AdditionGroup? {
        TODO("Not yet implemented")
    }

    override suspend fun updateAdditionGroup(
        updateAdditionGroup: UpdateAdditionGroup,
        token: String,
        additionGroupUuid: String
    ) {
        networkConnector.patchAdditionGroup(
            additionGroupUuid = additionGroupUuid,
            additionGroupPatchServer = updateAdditionGroup.mapUpdateAdditionGroupServerToPatchAdditionGroup(),
            token = token
        ).onSuccess { additionGroupServer ->
            updateLocalCache(
                uuid = additionGroupUuid,
                additionGroupServer = additionGroupServer
            )
        }
    }

    override fun clearCache() {
        additionGroupListCache = null
    }

    private fun updateLocalCache(
        uuid: String,
        additionGroupServer: AdditionGroupServer
    ) {
        additionGroupListCache = additionGroupListCache?.map { additionGroup ->
            if (uuid == additionGroup.uuid) {
                additionGroupServer.mapAdditionGroupServerToAddition()
            } else {
                additionGroup
            }
        }
    }
}
