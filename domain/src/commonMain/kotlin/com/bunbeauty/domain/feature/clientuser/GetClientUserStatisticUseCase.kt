package com.bunbeauty.domain.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.model.ClientUserStatistic
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class GetClientUserStatisticUseCase(
    private val clientUserRepo: ClientUserRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(clientUserUuid: String): ClientUserStatistic =
        clientUserRepo.getClientUserStatistic(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            clientUserUuid = clientUserUuid,
        )
}
