package com.bunbeauty.domain.feature.login

import com.bunbeauty.domain.exception.LoginException
import com.bunbeauty.domain.model.user.LoginUser
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test

class LoginUseCaseTest {

    private val userAuthorizationRepo: UserAuthorizationRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()

    private val loginUseCase = LoginUseCase(
        userAuthorizationRepo = userAuthorizationRepo,
        dataStoreRepo = dataStoreRepo
    )

    @Test
    fun `invoke should save token, cafeUuid, companyUuid, and username when login is successful`() =
        runTest {
            // Arrange
            val username = "testUser"
            val password = "testPassword"
            val token = "testToken"
            val cafeUuid = "testCafeUuid"
            val companyUuid = "testCompanyUuid"

            coEvery {
                userAuthorizationRepo.login(username = username, password = password)
            } returns LoginUser(token = token, cafeUuid = cafeUuid, companyUuid = companyUuid)

            coEvery { dataStoreRepo.saveToken(token) } returns Unit
            coEvery { dataStoreRepo.saveCafeUuid(cafeUuid) } returns Unit
            coEvery { dataStoreRepo.saveCompanyUuid(companyUuid) } returns Unit
            coEvery { dataStoreRepo.saveUsername(username) } returns Unit
            coEvery { userAuthorizationRepo.updateNotificationToken() } returns Unit

            // Act
            loginUseCase(username, password)

            // Assert
            coVerify {
                dataStoreRepo.saveToken(token)
                dataStoreRepo.saveCafeUuid(cafeUuid)
                dataStoreRepo.saveCompanyUuid(companyUuid)
                dataStoreRepo.saveUsername(username)
                userAuthorizationRepo.updateNotificationToken()
            }
        }

    @Test
    fun `invoke should throw LoginException when login fails`() = runTest {
        // Arrange
        val username = "testUser"
        val password = "testPassword"

        coEvery {
            userAuthorizationRepo.login(username = username, password = password)
        } returns null

        // Act & Assert
        assertThrows(LoginException::class.java) {
            runBlocking { loginUseCase(username, password) }
        }
    }
}
