package com.bunbeauty.domain.feature.orders

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetOrderListFlowUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val orderRepo: OrderRepo = mockk()
    private lateinit var getOrderListFlow: GetOrderListFlowUseCase

    @BeforeTest
    fun setup() {
        getOrderListFlow = GetOrderListFlowUseCase(
            dataStoreRepo = dataStoreRepo,
            orderRepo = orderRepo,
        )
    }

    @Test
    fun `return flow with filtered list without canceled orders`() = runTest {
        // Given
        val token = "token"
        val cafeUuid = "cafeUuid"
        coEvery { dataStoreRepo.token } returns flowOf(token)
        coEvery { orderRepo.getOrderListFlow(token, cafeUuid) } returns flowOf(
            listOf(
                createOrder(OrderStatus.NOT_ACCEPTED),
                createOrder(OrderStatus.ACCEPTED),
                createOrder(OrderStatus.PREPARING),
                createOrder(OrderStatus.SENT_OUT),
                createOrder(OrderStatus.DELIVERED),
                createOrder(OrderStatus.DONE),
                createOrder(OrderStatus.CANCELED),
            )
        )
        val filteredOrderList = arrayOf(
            createOrder(OrderStatus.NOT_ACCEPTED),
            createOrder(OrderStatus.ACCEPTED),
            createOrder(OrderStatus.PREPARING),
            createOrder(OrderStatus.SENT_OUT),
            createOrder(OrderStatus.DELIVERED),
            createOrder(OrderStatus.DONE),
        )

        // When
        val result = getOrderListFlow(cafeUuid)

        // Then
        assertContentEquals(filteredOrderList, result.first().toTypedArray())
    }

    private fun createOrder(orderStatus: OrderStatus): Order {
        return  Order(
            uuid = "uuid",
            code = "code",
            time = 0L,
            deferredTime = null,
            timeZone = "timeZone",
            orderStatus = orderStatus,
        )
    }
}