package com.bunbeauty.domain.model.cafe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CafeEntity(
        @PrimaryKey
        var uuid: String = "",
        val address: String,
        val latitude: Double,
        val longitude: Double,
        val fromTime: String,
        val toTime: String,
        val phone: String,
        val visible: Boolean,
)