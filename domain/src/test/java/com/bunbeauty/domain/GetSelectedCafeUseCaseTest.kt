package com.bunbeauty.domain

import com.bunbeauty.domain.feature.order_list.GetSelectedCafeUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.SelectedCafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetSelectedCafeUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val cafeRepo: CafeRepo = mockk()
    private lateinit var getSelectedCafe: GetSelectedCafeUseCase

    @BeforeTest
    fun setup() {
        getSelectedCafe = GetSelectedCafeUseCase(
            dataStoreRepo = dataStoreRepo,
            cafeRepo = cafeRepo,
        )
    }

    @Test
    fun `return first cafe when selected uuid is not saved`() = runTest {
        // Given
        val token = "token"
        val cityUuid = "cityUuid"
        coEvery { dataStoreRepo.token } returns flowOf(token)
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { dataStoreRepo.cafeUuid } returns flowOf(null)
        coEvery { cafeRepo.getCafeList(token, cityUuid) } returns listOf(
            createCafe("uuid1", "address1"),
            createCafe("uuid2", "address2"),
            createCafe("uuid3", "address3"),
        )
        val selectedCafe = SelectedCafe(
            uuid = "uuid1",
            address = "address1",
        )

        // When
        val result = getSelectedCafe()

        // Then
        assertEquals(selectedCafe, result)
    }

    @Test
    fun `return selected cafe when selected uuid is saved`() = runTest {
        // Given
        val token = "token"
        val cityUuid = "cityUuid"
        val cafeUuid = "uuid3"
        coEvery { dataStoreRepo.token } returns flowOf(token)
        coEvery { dataStoreRepo.managerCity } returns flowOf(cityUuid)
        coEvery { dataStoreRepo.cafeUuid } returns flowOf(cafeUuid)
        coEvery { cafeRepo.getCafeList(token, cityUuid) } returns listOf(
            createCafe("uuid1", "address1"),
            createCafe("uuid2", "address2"),
            createCafe("uuid3", "address3"),
        )
        val selectedCafe = SelectedCafe(
            uuid = "uuid3",
            address = "address3",
        )

        // When
        val result = getSelectedCafe()

        // Then
        assertEquals(selectedCafe, result)
    }

    private fun createCafe(uuid: String, address: String): Cafe {
        return Cafe(
            uuid = uuid,
            address = address,
            latitude = 0.0,
            longitude = 0.0,
            fromTime = 0,
            toTime = 0,
            phone = "phone",
            visible = true,
            cityUuid = "cityUuid",
        )
    }
}