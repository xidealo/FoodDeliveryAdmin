package com.bunbeauty.domain.feature.orderlist

class CheckIsAnotherCafeSelectedUseCase(
    private val getSelectedCafe: GetSelectedCafeUseCase
) {

    suspend operator fun invoke(cafeUuid: String): Boolean {
        val cafe = getSelectedCafe()

        return cafeUuid != cafe?.uuid
    }
}
