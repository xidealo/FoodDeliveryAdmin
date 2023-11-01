package com.bunbeauty.domain.feature.cafelist

import com.bunbeauty.domain.model.city.City
import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetTimeZoneByCityUuidUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val cityRepo: CityRepo = mockk()
    private lateinit var getTimeZoneByCityUuid: GetTimeZoneByCityUuidUseCase
    private val companyUuid = "companyUuid"
    private val cityUuid = "cityUuid"
    private val defaultTimeZone = "UTC+3"

    @BeforeTest
    fun setup() {
        getTimeZoneByCityUuid = GetTimeZoneByCityUuidUseCase(
            dataStoreRepo = dataStoreRepo,
            cityRepo = cityRepo,
        )
    }

    @Test
    fun `return default value when no company uuid`() = runTest {
        // Given
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

        // When
        val result = getTimeZoneByCityUuid(cityUuid)

        // Then
        assertEquals(defaultTimeZone, result)
    }

    @Test
    fun `return default value when city not found`() = runTest {
        // Given
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { cityRepo.getCityByUuid(companyUuid, cityUuid) } returns null

        // When
        val result = getTimeZoneByCityUuid(cityUuid)

        // Then
        assertEquals(defaultTimeZone, result)
    }

    @Test
    fun `return city time zone when city found`() = runTest {
        // Given
        val timeZone = "UTC-4"
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { cityRepo.getCityByUuid(companyUuid, cityUuid) } returns City(
            uuid = "uuid",
            name = "name",
            timeZone = timeZone,
            isVisible = true,
        )

        // When
        val result = getTimeZoneByCityUuid(cityUuid)

        // Then
        assertEquals(timeZone, result)
    }

}