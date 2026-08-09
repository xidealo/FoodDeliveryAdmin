package com.bunbeauty.domain.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
import com.bunbeauty.domain.feature.clientuser.validation.ValidateClientPhoneNumberUseCase
import com.bunbeauty.domain.feature.clientuser.validation.ValidatePercentDiscountUseCase
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class UpdateClientUserDiscountUseCase(
    private val clientUserRepo: ClientUserRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val validateClientPhoneNumberUseCase: ValidateClientPhoneNumberUseCase,
    private val validatePercentDiscountUseCase: ValidatePercentDiscountUseCase,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        percentDiscount: Int,
    ): ClientUserSettings {
        val validatedPhoneNumber = validateClientPhoneNumberUseCase(phoneNumber)
        val validatedPercentDiscount = validatePercentDiscountUseCase(percentDiscount)

        return clientUserRepo.updateClientUserDiscount(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            phoneNumber = validatedPhoneNumber,
            percentDiscount = validatedPercentDiscount,
        )
    }
}
