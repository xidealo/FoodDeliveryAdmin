package com.bunbeauty.domain.repo

import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettingsList
import com.bunbeauty.domain.feature.clientuser.model.ClientUserStatistic

interface ClientUserRepo {
    suspend fun getClientUserList(
        token: String,
        limit: Int,
        offset: Int,
    ): ClientUserSettingsList

    suspend fun getClientUserListByQuery(
        token: String,
        query: String,
        limit: Int,
        offset: Int,
    ): ClientUserSettingsList

    suspend fun getClientUserStatistic(
        token: String,
        clientUserUuid: String,
    ): ClientUserStatistic

    suspend fun updateClientUserProblematic(
        token: String,
        clientUserUuid: String,
        isProblematic: Boolean,
    ): ClientUserSettings
}
