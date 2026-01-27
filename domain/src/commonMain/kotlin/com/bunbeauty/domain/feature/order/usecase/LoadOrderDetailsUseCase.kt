package com.bunbeauty.domain.feature.order.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.repo.DataStoreRepo

class LoadOrderDetailsUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo,
) {
    suspend operator fun invoke(orderUuid: String): OrderDetails? {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        return orderRepo
            .loadOrderByUuid(
                token = token,
                orderUuid = orderUuid,
            )?.let { orderDetails ->
                orderDetails.copy(
                    oderProductList =
                        orderDetails.oderProductList.map { oderProduct ->
                            oderProduct.copy(
                                orderAdditions =
                                    oderProduct.orderAdditions.sortedBy { addition ->
                                        addition.priority
                                    },
                            )
                        },
                )
            }
    }
}
