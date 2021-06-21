package com.bunbeauty.domain.model

import androidx.room.PrimaryKey

data class Company(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var login: String
) {

    companion object {
        const val COMPANY: String = "COMPANY"
        const val PASSWORD: String = "password"
        const val TOKEN: String = "token"
    }
}