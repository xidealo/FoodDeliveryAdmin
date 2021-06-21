package com.bunbeauty.domain.model.cafe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CafeEntity(
        @PrimaryKey
        var id: String = ""
)