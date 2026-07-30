package com.bunbeauty.domain.repo

import com.bunbeauty.domain.feature.profile.model.UserRole

interface UserRepo {
    suspend fun getUserRole(): UserRole
}
