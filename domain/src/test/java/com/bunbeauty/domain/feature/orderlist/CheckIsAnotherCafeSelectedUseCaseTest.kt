package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.model.cafe.SelectedCafe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckIsAnotherCafeSelectedUseCaseTest {

    private val getSelectedCafe: GetSelectedCafeUseCase = mockk()
    private lateinit var checkIsAnotherCafeSelected: CheckIsAnotherCafeSelectedUseCase

    @BeforeTest
    fun setup() {
        checkIsAnotherCafeSelected = CheckIsAnotherCafeSelectedUseCase(getSelectedCafe)
    }

    @Test
    fun `return false when cafe uuid is the same`() = runTest {
        // Given
        val cafeUuid = "cafeUuid"
        val selectedCafe = SelectedCafe(
            uuid = cafeUuid,
            address = "address"
        )
        coEvery { getSelectedCafe() } returns selectedCafe

        // When
        val result = checkIsAnotherCafeSelected(cafeUuid)

        // Then
        assertFalse(result)
    }

    @Test
    fun `return true when cafe uuid is different`() = runTest {
        // Given
        val selectedCafe = SelectedCafe(
            uuid = "cafeUuid",
            address = "address"
        )
        coEvery { getSelectedCafe() } returns selectedCafe

        val newSelectedCafeUuid = "newSelectedCafeUuid"

        // When
        val result = checkIsAnotherCafeSelected(newSelectedCafeUuid)

        // Then
        assertTrue(result)
    }
}
