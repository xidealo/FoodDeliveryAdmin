package com.bunbeauty.domain.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushBodyException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushTitleException
import com.bunbeauty.domain.feature.clientuser.validation.ValidateClientPhoneNumberUseCase
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class SendClientPushUseCase(
    private val clientUserRepo: ClientUserRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val validateClientPhoneNumberUseCase: ValidateClientPhoneNumberUseCase,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        title: String,
        body: String,
    ) {
        val validatedPhoneNumber = validateClientPhoneNumberUseCase(phoneNumber)
        val validatedTitle = validateTitle(title)
        val validatedBody = validateBody(body)

        clientUserRepo.sendClientPush(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            phoneNumber = validatedPhoneNumber,
            title = validatedTitle,
            body = validatedBody,
        )
    }

    private fun validateTitle(title: String): String {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isNotEmpty()) {
            return trimmedTitle
        } else {
            throw ClientPushTitleException()
        }
    }

    private fun validateBody(body: String): String {
        val trimmedBody = body.trim()
        if (trimmedBody.isNotEmpty()) {
            return trimmedBody
        } else {
            throw ClientPushBodyException()
        }
    }
}
