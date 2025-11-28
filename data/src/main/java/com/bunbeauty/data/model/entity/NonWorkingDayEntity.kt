package com.bunbeauty.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NonWorkingDayEntity(
    @PrimaryKey
    val uuid: String,
    val timestamp: Long,
    val cafeUuid: String,
    val isVisible: Boolean
)
