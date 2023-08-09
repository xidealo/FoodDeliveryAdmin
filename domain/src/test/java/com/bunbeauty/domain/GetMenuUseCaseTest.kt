package com.bunbeauty.domain

import com.bunbeauty.domain.feature.order_list.CheckIsLastOrderUseCase
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.assertTrue
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMenuUseCaseTest {

    @BeforeTest
    fun setup() {

    }

    @Test
    fun `return sorted list by name when menu has more than one elements`() = runTest {
        // Given
        val orderCode = "orderCode"
        //coEvery { dataStoreRepo.lastOrderCode } returns flowOf(orderCode)

        // When
        //val result = checkIsLastOrder(orderCode)

        // Then
        //assertTrue(result)
    }

}
