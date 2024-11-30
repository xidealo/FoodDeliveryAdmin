package com.bunbeauty.domain.feature.cafelist

import com.bunbeauty.domain.exception.NoCityUuidException
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetCafeListByCityUuidUseCaseTest {
    private lateinit var getCafeListByCityUuidUseCase: GetCafeListByCityUuidUseCase
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val cafeRepo: CafeRepo = mockk()

    @BeforeTest
    fun setup() {
        getCafeListByCityUuidUseCase = GetCafeListByCityUuidUseCase(
            cafeRepo = cafeRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `should throw NoCityUuidException when managerCity is null`() = runTest {
        coEvery { dataStoreRepo.managerCity } returns flow {}

        assertFailsWith(
            exceptionClass = NoCityUuidException::class,
            block = { getCafeListByCityUuidUseCase() }
        )
    }

    @Test
    fun `should return city list when has cityUuid`() = runTest {
        val cityUuid = "cityUuid"
        // given
        coEvery { dataStoreRepo.managerCity } returns flow {
            emit(cityUuid)
        }
        coEvery {
            cafeRepo.getCafeListByCityUuid(cityUuid = cityUuid)
        } returns listOf(
            cafeMock.copy(
                uuid = "1",
                cityUuid = cityUuid
            ),
            cafeMock.copy(
                uuid = "2",
                cityUuid = cityUuid
            ),
            cafeMock.copy(
                uuid = "3",
                cityUuid = cityUuid
            )
        )
        // When
        val result = getCafeListByCityUuidUseCase()

        // Then
        assertEquals(
            listOf(
                cafeMock.copy(
                    uuid = "1",
                    cityUuid = cityUuid
                ),
                cafeMock.copy(
                    uuid = "2",
                    cityUuid = cityUuid
                ),
                cafeMock.copy(
                    uuid = "3",
                    cityUuid = cityUuid
                )
            ),
            result
        )
    }

    private val cafeMock = Cafe(
        uuid = "112",
        address = "address",
        latitude = 0.0,
        longitude = 0.0,
        fromTime = 0,
        toTime = 0,
        offset = 0,
        phone = "777777777",
        visible = true,
        cityUuid = ""
    )
}
