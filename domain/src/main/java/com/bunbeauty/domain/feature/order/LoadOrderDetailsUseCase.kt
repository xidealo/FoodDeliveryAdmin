package com.bunbeauty.domain.feature.order

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import javax.inject.Inject

class LoadOrderDetailsUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo,
) {

    suspend operator fun invoke(orderUuid: String): OrderDetails? {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        return orderRepo.loadOrderByUuid(
            token = token,
            orderUuid = orderUuid
        )?.let { orderDetails ->
            orderDetails.copy(
                oderProductList = orderDetails.oderProductList.map { oderProduct ->
                    oderProduct.copy(
                        additions = oderProduct.additions.sortedBy { addition ->
                            addition.priority
                        }
                    )
                }
            )
        }
    }
}