package com.bunbeauty.domain.feature.profile.model

enum class UserRole {
    MANAGER,
    ADMIN,
    COURIER,
    ;

    companion object {
        fun fromServer(role: String): UserRole? =
            when (role.lowercase()) {
                "manager" -> MANAGER
                "admin" -> ADMIN
                "courier" -> COURIER
                else -> null
            }

        fun toServer(role: UserRole): String =
            when (role) {
                MANAGER -> "manager"
                ADMIN -> "admin"
                COURIER -> "courier"
            }
    }
}
