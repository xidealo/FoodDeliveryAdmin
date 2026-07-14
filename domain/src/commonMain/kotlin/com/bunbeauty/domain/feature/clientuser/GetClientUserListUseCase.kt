package com.bunbeauty.domain.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettingsList
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class GetClientUserListUseCase(
    private val clientUserRepo: ClientUserRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(
        limit: Int,
        offset: Int,
    ): ClientUserSettingsList =
        clientUserRepo.getClientUserList(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            limit = limit,
            offset = offset,
        )
}
