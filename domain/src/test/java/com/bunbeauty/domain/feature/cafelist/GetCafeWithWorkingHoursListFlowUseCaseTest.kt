package com.bunbeauty.domain.feature.cafelist

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.feature.orderlist.GetCafeListUseCase
import com.bunbeauty.domain.feature.time.GetCurrentTimeFlowUseCase
import com.bunbeauty.domain.feature.time.Time
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetCafeWithWorkingHoursListFlowUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val getTimeZoneByCityUuid: GetTimeZoneByCityUuidUseCase = mockk()
    private val getCafeList: GetCafeListUseCase = mockk()
    private val getCurrentTimeFlow: GetCurrentTimeFlowUseCase = mockk()
    private lateinit var getCafeWithWorkingHoursListFlow: GetCafeWithWorkingHoursListFlowUseCase
    private val cityUuid = "cityUuid"
    private val timeZone = "UTC+3"
    private val cafe = Cafe(
        uuid = "uuid",
        address = "address",
        latitude = 0.0,
        longitude = 0.0,
        fromTime = 3600 * 8, // 8:00
        toTime = 3600 * 20, // 20:00
        phone = "phone",
        visible = true,
        cityUuid = "cityUuid",
    )
    private val nightCafe = Cafe(
        uuid = "uuid",
        address = "address",
        latitude = 0.0,
        longitude = 0.0,
        fromTime = 3600 * 20, // 20:00
        toTime = 3600 * 8, // 8:00
        phone = "phone",
        visible = true,
        cityUuid = "cityUuid",
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
        getCafeWithWorkingHoursListFlow = GetCafeWithWorkingHoursListFlowUseCase(
            dataStoreRepo = dataStoreRepo,
            getTimeZoneByCityUuid = getTimeZoneByCityUuid,
            getCafeList = getCafeList,
            getCurrentTimeFlow = getCurrentTimeFlow,
        )
    }

    @Test
    fun `throws exception when no manager city`() = runTest {
        // Given
        coEvery { dataStoreRepo.managerCity } returns emptyFlow()

        // When/Then
        assertFailsWith(DataNotFoundException::class) { getCafeWithWorkingHoursListFlow() }
    }

    @Test
    fun `returns empty cafe list flow when no cafes`() = runTest {
        // Given
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
        coEvery { getCafeList() } returns emptyList()
        val currentTime = Time(
            hour = 0,
            minute = 0,
            second = 0,
        )
        coEvery { getCurrentTimeFlow(timeZone, 60) } returns flowOf(currentTime)
        val expectedList = emptyList<CafeWithWorkingHours>()

        // When
        val result = getCafeWithWorkingHoursListFlow()

        // Then
        assertEquals(expectedList, result.first())
    }

    @Test
    fun `returns closed cafe list when time is after closing`() = runTest {
        // Given
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { getTimeZoneByCityUuid(cityUuid) } returns timeZone
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