package test.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.SendClientPushUseCase
import com.bunbeauty.domain.feature.clientuser.exception.ClientPhoneNumberException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushBodyException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushTitleException
import com.bunbeauty.domain.feature.clientuser.validation.ValidateClientPhoneNumberUseCase
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SendClientPushUseCaseTest {
    private val clientUserRepo: ClientUserRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val sendClientPushUseCase =
        SendClientPushUseCase(
            clientUserRepo = clientUserRepo,
            dataStoreRepo = dataStoreRepo,
            validateClientPhoneNumberUseCase = ValidateClientPhoneNumberUseCase(),
        )

    @Test
    fun `invoke sends push when token exists`() =
        runTest {
            val token = "valid_token"
            val phoneNumber = "+79001234567"
            val title = "Заголовок"
            val body = "Текст"

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                clientUserRepo.sendClientPush(
                    token = token,
                    phoneNumber = phoneNumber,
                    title = title,
                    body = body,
                )
            } just runs

            sendClientPushUseCase(
                phoneNumber = "  $phoneNumber  ",
                title = "  $title  ",
                body = "  $body  ",
            )

            coVerify {
                clientUserRepo.sendClientPush(
                    token = token,
                    phoneNumber = phoneNumber,
                    title = title,
                    body = body,
                )
            }
            coVerify { dataStoreRepo.getToken() }
        }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                sendClientPushUseCase(
                    phoneNumber = "+79001234567",
                    title = "Заголовок",
                    body = "Текст",
                )
            }

            coVerify(exactly = 0) {
                clientUserRepo.sendClientPush(
                    token = any(),
                    phoneNumber = any(),
                    title = any(),
                    body = any(),
                )
            }
        }

    @Test
    fun `invoke throws ClientPhoneNumberException when phone is invalid`() =
        runTest {
            assertFailsWith<ClientPhoneNumberException> {
                sendClientPushUseCase(
                    phoneNumber = "89001234567",
                    title = "Заголовок",
                    body = "Текст",
                )
            }

            coVerify(exactly = 0) { dataStoreRepo.getToken() }
            coVerify(exactly = 0) {
                clientUserRepo.sendClientPush(
                    token = any(),
                    phoneNumber = any(),
                    title = any(),
                    body = any(),
                )
            }
        }

    @Test
    fun `invoke throws ClientPushTitleException when title is blank`() =
        runTest {
            assertFailsWith<ClientPushTitleException> {
                sendClientPushUseCase(
                    phoneNumber = "+79001234567",
                    title = "   ",
                    body = "Текст",
                )
            }

            coVerify(exactly = 0) { dataStoreRepo.getToken() }
        }

    @Test
    fun `invoke throws ClientPushBodyException when body is blank`() =
        runTest {
            assertFailsWith<ClientPushBodyException> {
                sendClientPushUseCase(
                    phoneNumber = "+79001234567",
                    title = "Заголовок",
                    body = "   ",
                )
            }

            coVerify(exactly = 0) { dataStoreRepo.getToken() }
        }
}
