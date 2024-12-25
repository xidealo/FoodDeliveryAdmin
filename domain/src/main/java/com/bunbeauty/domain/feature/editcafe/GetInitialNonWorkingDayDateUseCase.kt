package com.bunbeauty.domain.feature.editcafe

import java.time.LocalDate

class GetInitialNonWorkingDayDateUseCase {

    operator fun invoke(): LocalDate {
        return LocalDate.now().plusDays(1)
    }
}
