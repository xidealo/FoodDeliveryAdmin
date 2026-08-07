package com.bunbeauty.domain.feature.clientuser.validation

import com.bunbeauty.domain.feature.clientuser.exception.ClientPhoneNumberException

class ValidateClientPhoneNumberUseCase {
    operator fun invoke(phoneNumber: String): String {
        val trimmedPhoneNumber = phoneNumber.trim()
        if (PHONE_NUMBER_REGEX.matches(trimmedPhoneNumber)) {
            return trimmedPhoneNumber
        } else {
            throw ClientPhoneNumberException()
        }
    }

    private companion object {
        val PHONE_NUMBER_REGEX = Regex("""^\+7[0-9]{10}$""")
    }
}
