package com.bunbeauty.domain.feature.deliveryzone

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.mapzonedelivery.GetDeliveryZoneUseCase
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetDeliveryZoneUseCaseTest {
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val cafeRepo: CafeRepo = mockk()

    lateinit var getDeliveryZoneUseCase: GetDeliveryZoneUseCase

    @BeforeTest
    fun setup() {
        getDeliveryZoneUseCase =
            GetDeliveryZoneUseCase(
                dataStoreRepo = dataStoreRepo,
                cafeRepo = cafeRepo,
            )
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                getDeliveryZoneUseCase()
            }
        }

    @Test
    fun `invoke() should throw NoCafeException when cafeUuid is missing`() =
        runTest {
            // Given
            val token = "token"

            coEvery { dataStoreRepo.getToken() } returns token
            every { dataStoreRepo.cafeUuid } returns flowOf()

            // When & Then
            assertFailsWith<NoCafeException> {
                getDeliveryZoneUseCase.invoke()
            }

            coVerify(exactly = 0) { cafeRepo.getDeliveryZone(any(), any(), any()) }
        }

    @Test
    fun `invoke successfully returns delivery zones`() =
        runTest {
            // Given
            val cafeUuid = "cafe_uuid"
            val token = "token"
            val expectedZones =
                listOf(
                    DeliveryZone.mock.copy(uuid = "zone_1"),
                    DeliveryZone.mock.copy(uuid = "zone_2"),
                )

            every { dataStoreRepo.cafeUuid } returns flowOf(cafeUuid)
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                cafeRepo.getPositionDeliveryZone(cafeUuid = cafeUuid, token = token)
            } returns expectedZones

            // When
            val result = getDeliveryZoneUseCase.invoke()

            // Then
            assertEquals(expectedZones, result)
            coVerify { cafeRepo.getPositionDeliveryZone(cafeUuid = cafeUuid, token = token) }
        }
}
