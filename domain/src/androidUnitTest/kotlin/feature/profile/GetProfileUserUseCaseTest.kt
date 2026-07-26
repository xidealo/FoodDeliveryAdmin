package test.feature.profile

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.feature.profile.GetProfileUserUseCase
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class GetProfileUserUseCaseTest {
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val userRepo: UserRepo = mockk()

    private val getProfileUserUseCase =
        GetProfileUserUseCase(
            dataStoreRepo = dataStoreRepo,
            userRepo = userRepo,
        )

    @Test
    fun `invoke should return profile user with role from repository`() =
        runTest {
            coEvery { dataStoreRepo.username } returns flowOf("courier_user")
            coEvery { userRepo.getUserRole() } returns UserRole.COURIER

            val result = getProfileUserUseCase()

            assertEquals(UserRole.COURIER, result.role)
            assertEquals("courier_user", result.userName)
        }

    @Test
    fun `invoke should throw DataNotFoundException when username is blank`() =
        runTest {
            coEvery { dataStoreRepo.username } returns flowOf("")

            assertThrows(DataNotFoundException::class.java) {
                runBlocking { getProfileUserUseCase() }
            }
        }
}
