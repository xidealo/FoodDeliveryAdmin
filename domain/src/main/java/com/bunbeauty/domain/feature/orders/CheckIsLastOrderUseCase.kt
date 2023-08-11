package com.bunbeauty.domain.feature.orders

import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CheckIsLastOrderUseCase @Inject constructor(private val dataStoreRepo: DataStoreRepo) {
    suspend operator fun invoke(orderCode: String): Boolean {
        return dataStoreRepo.lastOrderCode.firstOrNull() == orderCode
    }
}
