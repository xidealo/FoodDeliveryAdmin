package com.bunbeauty.domain.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class UpdateClientUserProblematicUseCase(
    private val clientUserRepo: ClientUserRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(
        clientUserUuid: String,
        isProblematic: Boolean,
    ): ClientUserSettings =
        clientUserRepo.updateClientUserProblematic(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            clientUserUuid = clientUserUuid,
            isProblematic = isProblematic,
        )
}
