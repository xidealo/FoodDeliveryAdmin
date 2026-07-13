package com.bunbeauty.domain.repo

import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettingsList

interface ClientUserRepo {
    suspend fun getClientUserList(
        token: String,
        limit: Int,
        offset: Int,
    ): ClientUserSettingsList
}
