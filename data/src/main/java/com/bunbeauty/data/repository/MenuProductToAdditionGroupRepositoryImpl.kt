package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.model.server.menproducttoadditiongroup.MenuProductToAdditionGroupServer
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.menuprocuttoadditiongroup.MenuProductToAdditionGroup
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository

class MenuProductToAdditionGroupRepositoryImpl(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val dataStoreRepo: DataStoreRepo
) : MenuProductToAdditionGroupRepository {
    private val cache: MutableSet<MenuProductToAdditionGroup> = mutableSetOf()

    override suspend fun getMenuProductToAdditionGroup(uuid: String): MenuProductToAdditionGroup? {
        return cache.find { menuProductToAdditionGroup ->
            menuProductToAdditionGroup.uuid == uuid
        } ?: getMenuProductToAdditionGroupNetwork(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            uuid = uuid,
        )?.toMenuProductToAdditionGroup().also { menuProductToAdditionGroup ->
            menuProductToAdditionGroup?.let {
                cache.add(menuProductToAdditionGroup)
            }
        }
    }

    private suspend fun getMenuProductToAdditionGroupNetwork(
        token: String,
        uuid: String
    ): MenuProductToAdditionGroupServer? {
        return foodDeliveryApi.getMenuProductToAdditionGroup(
            token = token,
            uuid = uuid
        ).dataOrNull()
    }

    private fun MenuProductToAdditionGroupServer.toMenuProductToAdditionGroup(): MenuProductToAdditionGroup {
        return MenuProductToAdditionGroup(
            uuid = uuid,
            menuProductUuid = menuProductUuid,
            additionGroupUuid = additionGroupUuid,
        )
    }

    override fun clearCache() {
        cache.clear()
    }
}