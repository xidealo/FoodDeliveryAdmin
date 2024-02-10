package com.bunbeauty.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CafeEntity(
    @PrimaryKey
    val uuid: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val fromTime: Int,
    val toTime: Int,
    val offset: Int,
    val phone: String,
    val visible: Boolean,
    val cityUuid: String
)