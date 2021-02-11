package com.bunbeauty.fooddeliveryadmin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CafeEntity(
        @PrimaryKey
        var id: String = ""
)