package test.feature.orderlist

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.feature.order.OrderUpdatesStreamEvent
import com.bunbeauty.domain.feature.orderlist.ObserveOrderListStreamUseCase
import com.bunbeauty.domain.feature.orderlist.OrderListStreamState
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ObserveOrderListStreamUseCaseTest {
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val orderRepo: OrderRepo = mockk()
    private lateinit var observeOrderListStream: ObserveOrderListStreamUseCase

    @BeforeTest
    fun setup() {
        observeOrderListStream =
            ObserveOrderListStreamUseCase(
                dataStoreRepo = dataStoreRepo,
                orderRepo = orderRepo,
            )
    }

    @Test
    fun `returns Orders state with all orders including canceled`() =
        runTest {
            val token = "token"
            val cafeUuid = "cafeUuid"
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { orderRepo.getOrderUpdatesStream(token, cafeUuid) } returns
                flowOf(
                    OrderUpdatesStreamEvent.Orders(
                        list =
                            listOf(
                                createOrder(OrderStatus.NOT_ACCEPTED),
                                createOrder(OrderStatus.CANCELED),
                            ),
                    ),
                )

            val firstState = observeOrderListStream(cafeUuid).first()

            val ordersState = firstState as OrderListStreamState.Orders
            assertEquals(2, ordersState.list.size)
            assertEquals(OrderStatus.NOT_ACCEPTED, ordersState.list.first().orderStatus)
            assertEquals(OrderStatus.CANCELED, ordersState.list.last().orderStatus)
        }

    private fun createOrder(orderStatus: OrderStatus): Order =
        Order(
            uuid = "uuid",
            code = "code",
            time = 0L,
            deferredTime = null,
            timeZone = "timeZone",
            orderStatus = orderStatus,
        )
}
