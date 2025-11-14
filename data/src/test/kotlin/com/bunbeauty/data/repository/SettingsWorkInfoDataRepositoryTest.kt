package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UpdateUnlimitedNotificationRequest
import com.bunbeauty.data.model.server.user.UserResponse
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsWorkInfoDataRepositoryTest {
    private val fakeToken = "token"
    private val dataStoreRepo: DataStoreRepo =
        mockk {
            coEvery { getToken() } returns fakeToken
        }
    private val foodDeliveryApi: FoodDeliveryApi = mockk()

    private lateinit var settingsRepository: SettingsRepository

    @BeforeTest
    fun setup() {
        settingsRepository =
            SettingsRepository(
                dataStoreRepo = dataStoreRepo,
                foodDeliveryApi = foodDeliveryApi,
            )
    }

    @Test
    fun `WHEN get unlimited notification and no cache THEN get unlimited notification from remote`() =
        runTest {
            val userResponseMock: UserResponse =
                mockk {
                    every { unlimitedNotification } returns false
                }
            coEvery {
                foodDeliveryApi.getUser(token = fakeToken)
            } returns ApiResult.Success(data = userResponseMock)

            val result = settingsRepository.isUnlimitedNotification()

            assertFalse(result)
            coVerify(exactly = 1) {
                foodDeliveryApi.getUser(token = fakeToken)
            }
        }

    @Test
    fun `WHEN get unlimited notification second time THEN return cache`() =
        runTest {
            val userResponseMock: UserResponse =
                mockk {
                    every { unlimitedNotification } returns false
                }
            coEvery {
                foodDeliveryApi.getUser(token = fakeToken)
            } returns ApiResult.Success(data = userResponseMock)

            // 1st time
            settingsRepository.isUnlimitedNotification()
            // 2nd time
            val result = settingsRepository.isUnlimitedNotification()

            assertFalse(result)
            coVerify(exactly = 1) {
                foodDeliveryApi.getUser(token = fakeToken)
            }
        }

    @Test
    fun `WHEN get unlimited notification and api call fails THEN return true by default`() =
        runTest {
            coEvery {
                foodDeliveryApi.getUser(token = fakeToken)
            } returns
                ApiResult.Error(
                    apiError =
                        ApiError(
                            message = "fail",
                        ),
                )

            val result = settingsRepository.isUnlimitedNotification()

            assertTrue(result)
            coVerify(exactly = 1) {
                foodDeliveryApi.getUser(token = fakeToken)
            }
        }

    @Test
    fun `WHEN update unlimited notification THEN call api and update cache`() =
        runTest {
            coJustRun {
                foodDeliveryApi.putUnlimitedNotification(
                    updateUnlimitedNotificationRequest = any(),
                    token = fakeToken,
                )
            }
            coEvery {
                foodDeliveryApi.getUser(token = fakeToken)
            } returns
                ApiResult.Error(
                    apiError =
                        ApiError(
                            message = "fail",
                        ),
                )

            settingsRepository.updateUnlimitedNotification(isEnabled = false)
            val result = settingsRepository.isUnlimitedNotification()

            assertFalse(result)
            coVerify(exactly = 0) {
                foodDeliveryApi.getUser(token = fakeToken)
            }
            coVerify(exactly = 1) {
                foodDeliveryApi.putUnlimitedNotification(
                    updateUnlimitedNotificationRequest =
                        UpdateUnlimitedNotificationRequest(
                            isEnabled = false,
                        ),
                    token = fakeToken,
                )
            }
        }
}
