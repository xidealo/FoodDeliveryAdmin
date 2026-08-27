package test.feature.clientuser

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.clientuser.UpdateClientUserProblematicUseCase
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
import com.bunbeauty.domain.repo.ClientUserRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateClientUserProblematicUseCaseTest {
    private val clientUserRepo: ClientUserRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val updateClientUserProblematicUseCase =
        UpdateClientUserProblematicUseCase(
            clientUserRepo = clientUserRepo,
            dataStoreRepo = dataStoreRepo,
        )

    @Test
    fun `invoke returns updated settings when token exists`() =
        runTest {
            val token = "valid_token"
            val clientUserUuid = "client-uuid"
            val isProblematic = true
            val updatedSettings =
                ClientUserSettings(
                    uuid = clientUserUuid,
                    phoneNumber = "+79999999901",
                    email = "user@example.com",
                    isActive = true,
                    isProblematic = isProblematic,
                )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                clientUserRepo.updateClientUserProblematic(
                    token = token,
                    clientUserUuid = clientUserUuid,
                    isProblematic = isProblematic,
                )
            } returns updatedSettings

            val result =
                updateClientUserProblematicUseCase(
                    clientUserUuid = clientUserUuid,
                    isProblematic = isProblematic,
                )

            assertEquals(updatedSettings, result)
            coVerify {
                clientUserRepo.updateClientUserProblematic(
                    token = token,
                    clientUserUuid = clientUserUuid,
                    isProblematic = isProblematic,
                )
            }
            coVerify { dataStoreRepo.getToken() }
        }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                updateClientUserProblematicUseCase(
                    clientUserUuid = "client-uuid",
                    isProblematic = true,
                )
            }

            coVerify(exactly = 0) {
                clientUserRepo.updateClientUserProblematic(
                    token = any(),
                    clientUserUuid = any(),
                    isProblematic = any(),
                )
            }
        }
}
