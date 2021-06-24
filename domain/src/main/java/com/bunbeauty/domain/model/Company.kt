package com.bunbeauty.domain.model

import androidx.room.PrimaryKey

data class Company(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var login: String
)