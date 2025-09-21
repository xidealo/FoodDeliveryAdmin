package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menuprocuttoadditiongroup.MenuProductToAdditionGroup

interface MenuProductToAdditionGroupRepository {
    suspend  fun getMenuProductToAdditionGroup(uuid: String): MenuProductToAdditionGroup?
    fun clearCache()
}