package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserRepo

class UserRepository(
    private val dataStoreRepo: DataStoreRepo,
    private val foodDeliveryApi: FoodDeliveryApi,
) : UserRepo {
    override suspend fun getUserRole(): UserRole {
        val cachedRole = dataStoreRepo.getUserRole()
        if (cachedRole != null) {
            val mappedCachedRole = UserRole.fromServer(cachedRole)
            if (mappedCachedRole != null) {
                return mappedCachedRole
            }
        }

        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val user = foodDeliveryApi.getUser(token = token).dataOrNull() ?: throw DataNotFoundException()
        val role = UserRole.fromServer(user.role) ?: throw DataNotFoundException()

        dataStoreRepo.saveUserRole(UserRole.toServer(role))

        return role
    }
}
