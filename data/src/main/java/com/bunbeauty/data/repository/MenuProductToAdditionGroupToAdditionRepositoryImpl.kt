package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.model.server.menuProductToAdditionGroupToAddition.MenuProductToAdditionGroupToAdditionServer
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.menuProcutToAdditionGroupToAddition.MenuProductToAdditionGroupToAddition
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupToAdditionRepository

class MenuProductToAdditionGroupToAdditionRepositoryImpl(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val dataStoreRepo: DataStoreRepo
) : MenuProductToAdditionGroupToAdditionRepository {
    private val cache: MutableSet<MenuProductToAdditionGroupToAddition> = mutableSetOf()

    // todo add handle error
    override suspend fun getMenuProductToAdditionGroupToAdditionList(uuidList: List<String>): List<MenuProductToAdditionGroupToAddition> {
        return foodDeliveryApi.getMenuProductToAdditionGroupToAdditionList(
            uuidList = uuidList,
            token = dataStoreRepo.getToken() ?: throw NoTokenException()
        ).dataOrNull()?.map { menuProductToAdditionGroupToAdditionServer ->
            menuProductToAdditionGroupToAdditionServer.toMenuProductToAdditionGroupToAddition()
        } ?: emptyList()
    }

    private fun MenuProductToAdditionGroupToAdditionServer.toMenuProductToAdditionGroupToAddition(): MenuProductToAdditionGroupToAddition {
        return MenuProductToAdditionGroupToAddition(
            uuid = uuid,
            isSelected = isSelected,
            isVisible = isVisible,
            menuProductToAdditionGroupUuid = menuProductToAdditionGroupUuid,
            additionUuid = additionUuid,
            priority = priority
        )
    }

    override fun clearCache() {
        cache.clear()
    }
}
