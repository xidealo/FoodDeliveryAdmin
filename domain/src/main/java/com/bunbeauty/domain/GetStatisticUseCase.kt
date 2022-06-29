package com.bunbeauty.domain

import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetStatisticUseCase @Inject constructor(
    private val statisticRepo: StatisticRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend fun invoke(
        cafeUuid: String,
        period: String,
        byDescending: Boolean,
        sortOption: StatisticSortOption?
    ): ApiResult<List<Statistic>> {
        return statisticRepo.getStatistic(dataStoreRepo.token.first(), cafeUuid, period)
            .let { apiResult ->
                if (apiResult is ApiResult.Success) {
                    if (sortOption == null) return apiResult
                    when (sortOption) {
                        StatisticSortOption.BY_ORDER_COUNT -> {
                            if (byDescending)
                                ApiResult.Success(apiResult.data.map { statistic ->
                                    statistic.copy(
                                        productStatisticList = statistic.productStatisticList.sortedByDescending { it.orderCount })
                                })
                            else
                                ApiResult.Success(apiResult.data.map { statistic ->
                                    statistic.copy(
                                        productStatisticList = statistic.productStatisticList.sortedBy { it.orderCount })
                                })
                        }
                        StatisticSortOption.BY_PRODUCT_COUNT -> {
                            if (byDescending)
                                ApiResult.Success(apiResult.data.map { statistic ->
                                    statistic.copy(
                                        productStatisticList = statistic.productStatisticList.sortedByDescending { it.productCount })
                                })
                            else
                                ApiResult.Success(apiResult.data.map { statistic ->
                                    statistic.copy(
                                        productStatisticList = statistic.productStatisticList.sortedBy { it.productCount })
                                })
                        }
                        StatisticSortOption.BY_PROCEEDS -> {
                            if (byDescending)
                                ApiResult.Success(apiResult.data.map { statistic ->
                                    statistic.copy(
                                        productStatisticList = statistic.productStatisticList.sortedByDescending { it.proceeds })
                                })
                            else
                                ApiResult.Success(apiResult.data.map { statistic ->
                                    statistic.copy(
                                        productStatisticList = statistic.productStatisticList.sortedBy { it.proceeds })
                                })
                        }
                    }
                } else
                    apiResult
            }
    }

    enum class StatisticSortOption {
        BY_ORDER_COUNT,
        BY_PRODUCT_COUNT,
        BY_PROCEEDS
    }
}

