package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.user.UserResponse
import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.domain.repo.DataStoreRepo
import common.ApiResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRepositoryTest {
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val foodDeliveryApi: FoodDeliveryApi = mockk()

    private val userRepository =
        UserRepository(
            dataStoreRepo = dataStoreRepo,
            foodDeliveryApi = foodDeliveryApi,
        )

    @Test
    fun `getUserRole returns cached role without network call`() =
        runTest {
            coEvery { dataStoreRepo.getUserRole() } returns "courier"

            val result = userRepository.getUserRole()

            assertEquals(UserRole.COURIER, result)
            coVerify(exactly = 0) { foodDeliveryApi.getUser(any()) }
        }

    @Test
    fun `getUserRole fetches and caches role when cache is empty`() =
        runTest {
            coEvery { dataStoreRepo.getUserRole() } returns null
            coEvery { dataStoreRepo.getToken() } returns "token"
            coEvery { foodDeliveryApi.getUser(token = "token") } returns
                ApiResult.Success(
                    UserResponse(
                        uuid = "uuid",
                        username = "courier_user",
                        role = "courier",
                        unlimitedNotification = true,
                    ),
                )
            coEvery { dataStoreRepo.saveUserRole("courier") } returns Unit

            val result = userRepository.getUserRole()

            assertEquals(UserRole.COURIER, result)
            coVerify(exactly = 1) { dataStoreRepo.saveUserRole("courier") }
        }

    @Test
    fun `getUserRole throws when server role is unknown`() =
        runTest {
            coEvery { dataStoreRepo.getUserRole() } returns null
            coEvery { dataStoreRepo.getToken() } returns "token"
            coEvery { foodDeliveryApi.getUser(token = "token") } returns
                ApiResult.Success(
                    UserResponse(
                        uuid = "uuid",
                        username = "user",
                        role = "unknown",
                        unlimitedNotification = true,
                    ),
                )

            assertThrows(DataNotFoundException::class.java) {
                runBlocking { userRepository.getUserRole() }
            }
            coVerify(exactly = 0) { dataStoreRepo.saveUserRole(any()) }
        }
}
