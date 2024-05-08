package com.bunbeauty.domain.feature.editcafe

import java.time.LocalDate
import javax.inject.Inject

class GetInitialNonWorkingDayDateUseCase @Inject constructor() {

    operator fun invoke(): LocalDate {
        return LocalDate.now().plusDays(1)
    }
}
