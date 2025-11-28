package com.bunbeauty.domain.model.user

data class LoginUser(
    val token: String,
    val cafeUuid: String,
    val companyUuid: String
)
