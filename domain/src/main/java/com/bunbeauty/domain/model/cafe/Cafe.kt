package com.bunbeauty.domain.model.cafe

import androidx.room.Embedded
import androidx.room.Relation
import com.bunbeauty.domain.model.address.Address

data class Cafe(
    @Embedded
    val cafeEntity: CafeEntity = CafeEntity(),

    @Relation(parentColumn = "id", entityColumn = "cafeId")
    val address: Address? = Address()
) {

        companion object {
                const val CAFES = "cafes"
        }
}