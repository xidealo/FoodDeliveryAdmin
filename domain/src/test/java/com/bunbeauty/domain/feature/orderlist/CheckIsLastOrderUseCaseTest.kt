package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckIsLastOrderUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var checkIsLastOrder: CheckIsLastOrderUseCase

    @BeforeTest
    fun setup() {
        checkIsLastOrder = CheckIsLastOrderUseCase(dataStoreRepo)
    }

    @Test
    fun `return true when last order code is the same`() = runTest {
        // Given
        val orderCode = "orderCode"
        coEvery { dataStoreRepo.lastOrderCode } returns flowOf(orderCode)

        // When
        val result = checkIsLastOrder(orderCode)

        // Then
        assertTrue(result)
    }

    @Test
    fun `return true when last order code is different`() = runTest {
        // Given
        val orderCode = "orderCode"
        coEvery { dataStoreRepo.lastOrderCode } returns flowOf(orderCode)

        val anotherOrderCode = "anotherOrderCode"

        // When
        val result = checkIsLastOrder(anotherOrderCode)

        // Then
        assertFalse(result)
    }
}
