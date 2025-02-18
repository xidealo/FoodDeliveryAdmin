package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.feature.main.GetSelectedCafeUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.SelectedCafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSelectedCafeUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val cafeRepo: CafeRepo = mockk()
    private lateinit var getSelectedCafeUseCase: GetSelectedCafeUseCase

    @BeforeTest
    fun setup() {
        getSelectedCafeUseCase = GetSelectedCafeUseCase(
            dataStoreRepo = dataStoreRepo,
            cafeRepo = cafeRepo
        )
    }

    @Test
    fun `invoke should return SelectedCafe when cafeUuid and cafe are found`() = runTest {
        // Arrange
        val cafeUuid = "testCafeUuid"
        val cafe = createCafe(uuid = cafeUuid, address = "Test Address")
        val expectedSelectedCafe = SelectedCafe(uuid = cafeUuid, address = "Test Address")

        coEvery { dataStoreRepo.cafeUuid } returns flowOf(cafeUuid)
        coEvery { cafeRepo.getCafeByUuid(cafeUuid) } returns cafe

        // Act
        val result = getSelectedCafeUseCase()

        // Assert
        assertEquals(expectedSelectedCafe, result)
    }

    @Test
    fun `invoke should throw NoCafeException when cafeUuid is not found`() = runTest {
        // Arrange
        coEvery { dataStoreRepo.cafeUuid } returns flowOf(null)

        // Act & Assert
        assertThrows(NoCafeException::class.java) {
            runTest {
                getSelectedCafeUseCase()
            }
        }
    }

    @Test
    fun `invoke should throw NoCafeException when cafe is not found by uuid`() = runTest {
        // Arrange
        val cafeUuid = "testCafeUuid"
        coEvery { dataStoreRepo.cafeUuid } returns flowOf(cafeUuid)
        coEvery { cafeRepo.getCafeByUuid(cafeUuid) } returns null

        // Act & Assert
        assertThrows(NoCafeException::class.java) {
            runTest {
                getSelectedCafeUseCase()
            }
        }
    }
    private fun createCafe(uuid: String, address: String): Cafe {
        return Cafe(
            uuid = uuid,
            address = address,
            latitude = 0.0,
            longitude = 0.0,
            fromTime = 0,
            toTime = 0,
            offset = 3,
            phone = "phone",
            visible = true,
            cityUuid = "cityUuid"
        )
    }
}
