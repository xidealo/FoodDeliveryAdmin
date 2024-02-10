package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.feature.main.GetSelectedCafeFlowUseCase
import com.bunbeauty.domain.model.cafe.SelectedCafe
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetSelectedCafeUseCase @Inject constructor(
    private val getSelectedCafeFlow: GetSelectedCafeFlowUseCase,
) {

    suspend operator fun invoke(): SelectedCafe? {
        return getSelectedCafeFlow().firstOrNull()
    }

}
