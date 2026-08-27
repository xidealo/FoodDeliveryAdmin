package test.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.UpdateClientUserDiscountUseCase
import com.bunbeauty.domain.feature.clientuser.exception.ClientPhoneNumberException
import com.bunbeauty.domain.feature.clientuser.exception.PercentDiscountException
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
import com.bunbeauty.domain.feature.clientuser.validation.ValidateClientPhoneNumberUseCase
import com.bunbeauty.domain.feature.clientuser.validation.ValidatePercentDiscountUseCase
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateClientUserDiscountUseCaseTest {
    private val clientUserRepo: ClientUserRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val updateClientUserDiscountUseCase =
        UpdateClientUserDiscountUseCase(
            clientUserRepo = clientUserRepo,
            dataStoreRepo = dataStoreRepo,
            validateClientPhoneNumberUseCase = ValidateClientPhoneNumberUseCase(),
            validatePercentDiscountUseCase = ValidatePercentDiscountUseCase(),
        )

    @Test
    fun `invoke returns updated settings when token exists`() =
        runTest {
            val token = "valid_token"
            val phoneNumber = "+79001234567"
            val percentDiscount = 15
            val updatedSettings =
                ClientUserSettings(
                    uuid = "client-uuid",
                    phoneNumber = phoneNumber,
                    email = "user@example.com",
                    isActive = true,
                    isProblematic = false,
                    personalDiscountPercent = percentDiscount,
                )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                clientUserRepo.updateClientUserDiscount(
                    token = token,
                    phoneNumber = phoneNumber,
                    percentDiscount = percentDiscount,
                )
            } returns updatedSettings

            val result =
                updateClientUserDiscountUseCase(
                    phoneNumber = "  $phoneNumber  ",
                    percentDiscount = percentDiscount,
                )

            assertEquals(updatedSettings, result)
            coVerify {
                clientUserRepo.updateClientUserDiscount(
                    token = token,
                    phoneNumber = phoneNumber,
                    percentDiscount = percentDiscount,
                )
            }
            coVerify { dataStoreRepo.getToken() }
        }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                updateClientUserDiscountUseCase(
                    phoneNumber = "+79001234567",
                    percentDiscount = 15,
                )
            }

            coVerify(exactly = 0) {
                clientUserRepo.updateClientUserDiscount(
                    token = any(),
                    phoneNumber = any(),
                    percentDiscount = any(),
                )
            }
        }

    @Test
    fun `invoke throws ClientPhoneNumberException when phone is invalid`() =
        runTest {
            assertFailsWith<ClientPhoneNumberException> {
                updateClientUserDiscountUseCase(
                    phoneNumber = "89001234567",
                    percentDiscount = 15,
                )
            }

            coVerify(exactly = 0) { dataStoreRepo.getToken() }
            coVerify(exactly = 0) {
                clientUserRepo.updateClientUserDiscount(
                    token = any(),
                    phoneNumber = any(),
                    percentDiscount = any(),
                )
            }
        }

    @Test
    fun `invoke throws PercentDiscountException when percent is negative`() =
        runTest {
            assertFailsWith<PercentDiscountException> {
                updateClientUserDiscountUseCase(
                    phoneNumber = "+79001234567",
                    percentDiscount = -1,
                )
            }

            coVerify(exactly = 0) { dataStoreRepo.getToken() }
            coVerify(exactly = 0) {
                clientUserRepo.updateClientUserDiscount(
                    token = any(),
                    phoneNumber = any(),
                    percentDiscount = any(),
                )
            }
        }

    @Test
    fun `invoke throws PercentDiscountException when percent is 100`() =
        runTest {
            assertFailsWith<PercentDiscountException> {
                updateClientUserDiscountUseCase(
                    phoneNumber = "+79001234567",
                    percentDiscount = 100,
                )
            }

            coVerify(exactly = 0) { dataStoreRepo.getToken() }
            coVerify(exactly = 0) {
                clientUserRepo.updateClientUserDiscount(
                    token = any(),
                    phoneNumber = any(),
                    percentDiscount = any(),
                )
            }
        }
}
