package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.feature.profile.model.ProfileUser
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserRepo
import kotlinx.coroutines.flow.firstOrNull

class GetProfileUserUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val userRepo: UserRepo,
) {
    suspend operator fun invoke(): ProfileUser {
        val userName =
            dataStoreRepo.username.firstOrNull()?.takeIf { username ->
                username.isNotBlank()
            } ?: throw DataNotFoundException()
        val role = userRepo.getUserRole()

        return ProfileUser(
            role = role,
            userName = userName,
        )
    }
}
