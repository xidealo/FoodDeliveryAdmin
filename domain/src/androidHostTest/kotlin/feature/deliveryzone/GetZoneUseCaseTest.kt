package test.feature.deliveryzone

import com.bunbeauty.domain.exception.GetZoneException
import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.GetZoneUseCase
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

class GetZoneUseCaseTest {
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val cafeRepo: CafeRepo = mockk()

    private lateinit var getZoneUseCase: GetZoneUseCase

    @BeforeTest
    fun setup() {
        getZoneUseCase =
            GetZoneUseCase(
                dataStoreRepo = dataStoreRepo,
                cafeRepo = cafeRepo,
            )
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                getZoneUseCase("uuid")
            }
        }

    @Test
    fun `invoke() should throw NoCafeException when cafeUuid is missing`() =
        runTest {
            // Given
            val token = "token"
            val zoneUuid = "uuid"

            coEvery { dataStoreRepo.getToken() } returns token
            every { dataStoreRepo.cafeUuid } returns flowOf()

            // When & Then
            assertFailsWith<NoCafeException> {
                getZoneUseCase.invoke(zoneUuid)
            }

            coVerify(exactly = 0) { cafeRepo.getDeliveryZone(any(), any(), any()) }
        }

    @Test
    fun `invoke should throw Exception when cafeRepo returns null`() =
        runTest {
            // Given
            val token = "token"
            val cafeUuid = "cafe_uuid"
            val zoneUuid = "zone_uuid"

            coEvery { dataStoreRepo.getToken() } returns token
            every { dataStoreRepo.cafeUuid } returns flowOf(cafeUuid)
            coEvery {
                cafeRepo.getDeliveryZone(
                    cafeUuid = cafeUuid,
                    zoneUuid = zoneUuid,
                    token = token,
                )
            } returns null

            // When & Then
            assertFailsWith<GetZoneException> {
                getZoneUseCase.invoke(zoneUuid)
            }

            coVerify {
                cafeRepo.getDeliveryZone(
                    cafeUuid = cafeUuid,
                    zoneUuid = zoneUuid,
                    token = token,
                )
            }
        }

    @Test
    fun `invoke successfully returns delivery zone`() =
        runTest {
            // Given
            val token = "token"
            val cafeUuid = "cafe_uuid"
            val zoneUuid = "zone_uuid"
            val expectedZone = DeliveryZone.mock.copy(uuid = zoneUuid)

            coEvery { dataStoreRepo.getToken() } returns token
            every { dataStoreRepo.cafeUuid } returns flowOf(cafeUuid)
            coEvery {
                cafeRepo.getDeliveryZone(
                    cafeUuid = cafeUuid,
                    zoneUuid = zoneUuid,
                    token = token,
                )
            } returns expectedZone

            // When
            val result = getZoneUseCase.invoke(zoneUuid)

            // Then
            assertEquals(expectedZone, result)
            coVerify { dataStoreRepo.getToken() }
            coVerify {
                cafeRepo.getDeliveryZone(
                    cafeUuid = cafeUuid,
                    zoneUuid = zoneUuid,
                    token = token,
                )
            }
        }
}
