package com.bunbeauty.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val priority: Int,
)