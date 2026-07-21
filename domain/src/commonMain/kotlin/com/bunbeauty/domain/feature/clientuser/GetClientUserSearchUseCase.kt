package com.bunbeauty.domain.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettingsList
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class GetClientUserSearchUseCase(
    private val clientUserRepo: ClientUserRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(
        query: String,
        limit: Int,
        offset: Int,
    ): ClientUserSettingsList =
        clientUserRepo.getClientUserListByQuery(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            query = query,
            limit = limit,
            offset = offset,
        )
}
