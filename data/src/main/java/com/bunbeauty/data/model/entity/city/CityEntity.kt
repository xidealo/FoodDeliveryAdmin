package com.bunbeauty.data.model.entity.city

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityEntity(
    @PrimaryKey
    val uuid: String = "",
    val name: String,
    val timeZone: String,
    val isVisible: Boolean,
)