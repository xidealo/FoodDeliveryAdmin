package com.bunbeauty.domain.feature.cafelist

import com.bunbeauty.domain.feature.common.GetCafeListUseCase
import com.bunbeauty.domain.feature.time.GetCurrentTimeFlowUseCase
import com.bunbeauty.domain.feature.time.Time
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours
import com.bunbeauty.domain.util.datetime.IDateTimeUtil
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class GetCafeWithWorkingHoursListFlowUseCaseTest {

    private val getCafeList: GetCafeListUseCase = mockk()
    private val getCurrentTimeFlow: GetCurrentTimeFlowUseCase = mockk()
    private val dateTimeUtil: IDateTimeUtil = mockk()
    private lateinit var getCafeWithWorkingHoursListFlow: GetCafeWithWorkingHoursListFlowUseCase

    private val cityUuid = "cityUuid"
    private val timeZone = 3
    private val eightAm = 3600 * 8 // 8:00
    private val eightPm = 3600 * 20 // 20:00

    private val cafe = Cafe(
        uuid = "uuid",
        address = "address",
        latitude = 0.0,
        longitude = 0.0,
        fromTime = eightAm, // 8:00
        toTime = eightPm, // 20:00
        offset = timeZone,
        phone = "phone",
        visible = true,
        cityUuid = cityUuid,
    )
    private val nightCafe = Cafe(
        uuid = "uuid",
        address = "address",
        latitude = 0.0,
        longitude = 0.0,
        fromTime = eightPm, // 20:00
        toTime = eightAm, // 8:00
        offset = timeZone,
        phone = "phone",
        visible = true,
        cityUuid = cityUuid,
    )
    private val cafeWithWorkingHours = CafeWithWorkingHours(
        uuid = "uuid",
        address = "address",
        workingHours = "8:00 - 20:00",
        status = CafeStatus.Open,
        cityUuid = cityUuid,
    )
    private val nightCafeWithWorkingHours = CafeWithWorkingHours(
        uuid = "uuid",
        address = "address",
        workingHours = "20:00 - 8:00",
        status = CafeStatus.Open,
        cityUuid = cityUuid,
    )

    @BeforeTest
    fun setup() {
        coEvery { dateTimeUtil.getTimeHHMM(eightAm) } returns "8:00"
        coEvery { dateTimeUtil.getTimeHHMM(eightPm) } returns "20:00"
        getCafeWithWorkingHoursListFlow = GetCafeWithWorkingHoursListFlowUseCase(
            getCafeList = getCafeList,
            getCurrentTimeFlow = getCurrentTimeFlow,
            dateTimeUtil = dateTimeUtil,
        )
    }

    @Test
    fun `not emits values when no cafes`() = runTest {
        // Given
        coEvery { getCafeList() } returns emptyList()
        val currentTime = Time(
            hour = 0,
            minute = 0,
            second = 0,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertNull(result.firstOrNull())
    }

    @Test
    fun `returns closed cafe list when time is after closing`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(cafe)
        val currentTime = Time(
            hour = 1,
            minute = 0,
            second = 1,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(
            cafeWithWorkingHours.copy(status = CafeStatus.Closed)
        )

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns closed soon cafe list when time is less than hour before closing`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(cafe)
        val currentTime = Time(
            hour = 19,
            minute = 30,
            second = 1,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(
            cafeWithWorkingHours.copy(
                status = CafeStatus.CloseSoon(30)
            )
        )

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns open cafe list when time is between opening and closing`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(cafe)
        val currentTime = Time(
            hour = 12,
            minute = 0,
            second = 0,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(cafeWithWorkingHours)

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns closed cafe list when time is before opening`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(cafe)
        val currentTime = Time(
            hour = 7,
            minute = 59,
            second = 59,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(cafeWithWorkingHours.copy(status = CafeStatus.Closed))

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns open night cafe list when time is before closing`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(nightCafe)
        val currentTime = Time(
            hour = 1,
            minute = 0,
            second = 30,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(nightCafeWithWorkingHours)

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns closed soon night cafe list when time is less than hour before closing`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(nightCafe)
        val currentTime = Time(
            hour = 7,
            minute = 1,
            second = 0,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(
            nightCafeWithWorkingHours.copy(
                status = CafeStatus.CloseSoon(59)
            )
        )

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns closed night cafe list when time is between closing and opening`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(nightCafe)
        val currentTime = Time(
            hour = 12,
            minute = 30,
            second = 0,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(nightCafeWithWorkingHours.copy(status = CafeStatus.Closed))

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns open night cafe list when time is after opening`() = runTest {
        // Given
        coEvery { getCafeList() } returns listOf(nightCafe)
        val currentTime = Time(
            hour = 20,
            minute = 0,
            second = 1,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = listOf(nightCafeWithWorkingHours)

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

}