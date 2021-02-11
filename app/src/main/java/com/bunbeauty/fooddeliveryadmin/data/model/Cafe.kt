package com.bunbeauty.fooddeliveryadmin.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class Cafe(
        @Embedded
        val cafeEntity: CafeEntity = CafeEntity(),

        @Relation(parentColumn = "id", entityColumn = "cafeId")
        val address: Address = Address()
) {

        companion object {
                const val CAFES = "cafes"
        }
}