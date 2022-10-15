package com.bunbeauty.fooddeliveryadmin.shared.cafe

import javax.inject.Inject

class CafeSelector @Inject constructor() {

    fun selectCafe(cafeList: List<CafeUi>, cafeUuid: String?): List<CafeUi> {
        return cafeList.map { cafe ->
            if (cafe.uuid == cafeUuid) {
                cafe.copy(isSelected = true)
            } else {
                cafe.copy(isSelected = false)
            }
        }
    }
}