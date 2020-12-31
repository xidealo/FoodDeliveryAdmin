package com.bunbeauty.fooddeliveryadmin.data.model

import androidx.room.PrimaryKey

data class Company(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var uuid: String = "",
    var login: String
) : BaseModel() {

    companion object {
        const val COMPANY: String = "company"
        const val LOGIN: String = "login"
        const val PASSWORD: String = "password"
        const val TOKEN: String = "token"
    }
}