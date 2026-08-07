package com.bunbeauty.domain.feature.clientuser.model

data class ClientUserSettings(
    val uuid: String,
    val phoneNumber: String,
    val email: String?,
    val isActive: Boolean,
    val isProblematic: Boolean = false,
    val personalDiscountPercent: Int? = null,
)
