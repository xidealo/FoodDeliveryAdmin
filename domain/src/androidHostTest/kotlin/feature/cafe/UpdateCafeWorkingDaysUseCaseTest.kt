package test.feature.cafe

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.cafe.InvalidCafeWorkingHoursException
import com.bunbeauty.domain.feature.cafe.UpdateCafeWorkingDaysUseCase
import com.bunbeauty.domain.model.cafe.CafeWorkingDay
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UpdateCafeWorkingDaysUseCaseTest {
    private val cafeRepo: CafeRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var updateCafeWorkingDaysUseCase: UpdateCafeWorkingDaysUseCase

    @BeforeTest
    fun setUp() {
        updateCafeWorkingDaysUseCase =
            UpdateCafeWorkingDaysUseCase(
                cafeRepo = cafeRepo,
                dataStoreRepo = dataStoreRepo,
            )
    }

    @Test
    fun `invoke() should patch cafe with workingDays`() =
        runTest {
            val cafeUuid = "cafe-uuid"
            val token = "token"
            val workingDays = fullWeek()
            coEvery { dataStoreRepo.cafeUuid } returns flowOf(cafeUuid)
            coEvery { dataStoreRepo.getToken() } returns token

            updateCafeWorkingDaysUseCase(workingDays)

            coVerify {
                cafeRepo.patchCafe(
                    cafeUuid = cafeUuid,
                    updateCafe =
                        UpdateCafe(
                            workingDays = workingDays,
                        ),
                    token = token,
                )
            }
        }

    @Test
    fun `invoke() should throw InvalidCafeWorkingHoursException when fromTime is not before toTime`() =
        runTest {
            val workingDays =
                fullWeek().map { workingDay ->
                    if (workingDay.dayOfWeek == 1) {
                        workingDay.copy(
                            fromTime = 72_000,
                            toTime = 36_000,
                        )
                    } else {
                        workingDay
                    }
                }

            assertFailsWith<InvalidCafeWorkingHoursException> {
                updateCafeWorkingDaysUseCase(workingDays)
            }

            coVerify(exactly = 0) { cafeRepo.patchCafe(any(), any(), any()) }
        }

    @Test
    fun `invoke() should throw NoCafeException when cafeUuid is missing`() =
        runTest {
            val workingDays = fullWeek()
            coEvery { dataStoreRepo.cafeUuid } returns emptyFlow()

            assertFailsWith<NoCafeException> {
                updateCafeWorkingDaysUseCase(workingDays)
            }

            coVerify(exactly = 0) { cafeRepo.patchCafe(any(), any(), any()) }
        }

    @Test
    fun `invoke() should throw NoTokenException when token is null`() =
        runTest {
            val workingDays = fullWeek()
            coEvery { dataStoreRepo.cafeUuid } returns flowOf("cafe-uuid")
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                updateCafeWorkingDaysUseCase(workingDays)
            }

            coVerify(exactly = 0) { cafeRepo.patchCafe(any(), any(), any()) }
        }

    @Test
    fun `invoke() should throw InvalidCafeWorkingHoursException when week is incomplete`() =
        runTest {
            val workingDays =
                listOf(
                    CafeWorkingDay(
                        dayOfWeek = 1,
                        fromTime = 36_000,
                        toTime = 72_000,
                    ),
                )

            assertFailsWith<InvalidCafeWorkingHoursException> {
                updateCafeWorkingDaysUseCase(workingDays)
            }

            coVerify(exactly = 0) { cafeRepo.patchCafe(any(), any(), any()) }
        }

    private fun fullWeek(): List<CafeWorkingDay> =
        (1..7).map { dayOfWeek ->
            CafeWorkingDay(
                dayOfWeek = dayOfWeek,
                fromTime = 36_000,
                toTime = 72_000,
            )
        }
}
