package test.feature.login

import com.bunbeauty.domain.feature.login.CheckAuthorizationUseCase
import com.bunbeauty.domain.feature.login.SessionValidationResult
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.domain.usecase.LogoutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckAuthorizationUseCaseTest {
    private val userAuthorizationRepo: UserAuthorizationRepo = mockk(relaxUnitFun = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val logoutUseCase: LogoutUseCase = mockk(relaxUnitFun = true)

    private val checkAuthorizationUseCase =
        CheckAuthorizationUseCase(
            userAuthorizationRepo = userAuthorizationRepo,
            dataStoreRepo = dataStoreRepo,
            logoutUseCase = logoutUseCase,
        )

    @Test
    fun `returns false when token is missing`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            val isAuthorized = checkAuthorizationUseCase()

            assertFalse(isAuthorized)
            coVerify(exactly = 0) {
                userAuthorizationRepo.validateSession()
            }
            coVerify(exactly = 0) {
                logoutUseCase()
            }
        }

    @Test
    fun `returns true and updates notification token when session is valid`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns "token"
            coEvery { userAuthorizationRepo.validateSession() } returns SessionValidationResult.VALID

            val isAuthorized = checkAuthorizationUseCase()

            assertTrue(isAuthorized)
            verify(exactly = 1) {
                userAuthorizationRepo.updateNotificationToken()
            }
            coVerify(exactly = 0) {
                logoutUseCase()
            }
        }

    @Test
    fun `logs out and returns false when session is invalid`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns "token"
            coEvery { userAuthorizationRepo.validateSession() } returns SessionValidationResult.INVALID
            coEvery { logoutUseCase() } returns Unit

            val isAuthorized = checkAuthorizationUseCase()

            assertFalse(isAuthorized)
            coVerify(exactly = 1) {
                logoutUseCase()
            }
            verify(exactly = 0) {
                userAuthorizationRepo.updateNotificationToken()
            }
        }

    @Test
    fun `returns true without logout when session check is unavailable`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns "token"
            coEvery { userAuthorizationRepo.validateSession() } returns SessionValidationResult.UNAVAILABLE

            val isAuthorized = checkAuthorizationUseCase()

            assertTrue(isAuthorized)
            coVerify(exactly = 0) {
                logoutUseCase()
            }
            verify(exactly = 0) {
                userAuthorizationRepo.updateNotificationToken()
            }
        }
}
