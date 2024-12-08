package com.bunbeauty.domain.feature.orderlist

import javax.inject.Inject

class CheckIsAnotherCafeSelectedUseCase @Inject constructor(
    private val getSelectedCafe: GetSelectedCafeUseCase
) {

    suspend operator fun invoke(cafeUuid: String): Boolean {
        val cafe = getSelectedCafe()

        return cafeUuid != cafe?.uuid
    }
}
