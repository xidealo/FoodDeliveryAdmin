package test.feature.cafe

import com.bunbeauty.domain.feature.cafe.GetCafeWorkingDaysUseCase
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.CafeWorkingDay
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCafeWorkingDaysUseCaseTest {
    private val getCafeUseCase: GetCafeUseCase = mockk()
    private lateinit var getCafeWorkingDaysUseCase: GetCafeWorkingDaysUseCase

    @BeforeTest
    fun setUp() {
        getCafeWorkingDaysUseCase =
            GetCafeWorkingDaysUseCase(
                getCafeUseCase = getCafeUseCase,
            )
    }

    @Test
    fun `invoke() should return cafe workingDays when week is present`() =
        runTest {
            val workingDays =
                (1..7).map { dayOfWeek ->
                    CafeWorkingDay(
                        dayOfWeek = dayOfWeek,
                        fromTime = 10_000 * dayOfWeek,
                        toTime = 20_000 * dayOfWeek,
                    )
                }
            coEvery { getCafeUseCase() } returns cafe(workingDays = workingDays)

            val result = getCafeWorkingDaysUseCase()

            assertEquals(workingDays, result)
        }

    @Test
    fun `invoke() should fill missing days from cafe fromTime and toTime`() =
        runTest {
            val cafe =
                cafe(
                    fromTime = 36_000,
                    toTime = 72_000,
                    workingDays =
                        listOf(
                            CafeWorkingDay(
                                dayOfWeek = 1,
                                fromTime = 40_000,
                                toTime = 80_000,
                            ),
                        ),
                )
            coEvery { getCafeUseCase() } returns cafe

            val result = getCafeWorkingDaysUseCase()

            assertEquals(7, result.size)
            assertEquals(
                CafeWorkingDay(
                    dayOfWeek = 1,
                    fromTime = 40_000,
                    toTime = 80_000,
                ),
                result.first(),
            )
            result.drop(1).forEachIndexed { index, workingDay ->
                assertEquals(index + 2, workingDay.dayOfWeek)
                assertEquals(36_000, workingDay.fromTime)
                assertEquals(72_000, workingDay.toTime)
            }
        }

    private fun cafe(
        fromTime: Int = 0,
        toTime: Int = 0,
        workingDays: List<CafeWorkingDay> = emptyList(),
    ): Cafe =
        Cafe(
            uuid = "uuid",
            address = "address",
            latitude = 0.0,
            longitude = 0.0,
            fromTime = fromTime,
            toTime = toTime,
            offset = 0,
            phone = "123",
            visible = true,
            additional = false,
            cityUuid = "cityUuid",
            workload = WorkLoad.LOW,
            workType = WorkType.DELIVERY,
            workingDays = workingDays,
        )
}
