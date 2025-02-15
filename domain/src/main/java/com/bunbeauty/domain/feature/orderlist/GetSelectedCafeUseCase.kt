package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.feature.main.GetSelectedCafeFlowUseCase
import com.bunbeauty.domain.model.cafe.SelectedCafe
import kotlinx.coroutines.flow.firstOrNull

class GetSelectedCafeUseCase(
    private val getSelectedCafeFlow: GetSelectedCafeFlowUseCase
) {

    suspend operator fun invoke(): SelectedCafe? {
        return getSelectedCafeFlow().firstOrNull()
    }
}
