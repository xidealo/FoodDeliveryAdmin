package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menuProcutToAdditionGroupToAddition.MenuProductToAdditionGroupToAddition

interface MenuProductToAdditionGroupToAdditionRepository {
    suspend fun getMenuProductToAdditionGroupToAdditionList(uuidList: List<String>): List<MenuProductToAdditionGroupToAddition>

    fun clearCache()
}
