package com.bunbeauty.domain.feature.editcafe

import java.time.LocalDate

class GetMinNonWorkingDayDateUseCase {
    operator fun invoke(): LocalDate = LocalDate.now()
}
