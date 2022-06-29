package com.bunbeauty.domain

import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.model.statistic.ProductStatistic
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito


internal class GetStatisticUseCaseTest {

    private var statisticRepo: StatisticRepo =
        Mockito.mock(StatisticRepo::class.java)

    private var dataStoreRepo: DataStoreRepo =
        Mockito.mock(DataStoreRepo::class.java)

    private val getStatisticUseCase = GetStatisticUseCase(
        statisticRepo = statisticRepo,
        dataStoreRepo = dataStoreRepo
    )


    @Test
    fun `return not sorted statistic list if not select sort `() = runTest {

        Mockito.`when`(statisticRepo.getStatistic("token", "cafeUuid", period = "1"))
            .thenReturn(
                ApiResult.Success(
                    statisticList
                )
            )

        Mockito.`when`(dataStoreRepo.token)
            .thenReturn(
                flow {
                    emit("token")
                }
            )

        Assert.assertEquals(
            ApiResult.Success(statisticList),
            getStatisticUseCase.invoke(
                "cafeUuid",
                period = "1",
                true,
                null
            )
        )
    }

 @Test
    fun `return statistic list sorted by order count desc`() = runTest {

        Mockito.`when`(statisticRepo.getStatistic("token", "cafeUuid", period = "1"))
            .thenReturn(
                ApiResult.Success(
                    statisticList
                )
            )

        Mockito.`when`(dataStoreRepo.token)
            .thenReturn(
                flow {
                    emit("token")
                }
            )

        Assert.assertEquals(
            ApiResult.Success(statisticListSortedByOrderCountDesc),
            getStatisticUseCase.invoke(
                "cafeUuid",
                period = "1",
                true,
                GetStatisticUseCase.StatisticSortOption.BY_ORDER_COUNT
            )
        )
    }

    @Test
    fun `return statistic list sorted by order count asc`() = runTest {

        Mockito.`when`(statisticRepo.getStatistic("token", "cafeUuid", period = "1"))
            .thenReturn(
                ApiResult.Success(
                    statisticList
                )
            )

        Mockito.`when`(dataStoreRepo.token)
            .thenReturn(
                flow {
                    emit("token")
                }
            )

        Assert.assertEquals(
            ApiResult.Success(statisticListSortedByOrderCountAsc),
            getStatisticUseCase.invoke(
                "cafeUuid",
                period = "1",
                false,
                GetStatisticUseCase.StatisticSortOption.BY_ORDER_COUNT
            )
        )
    }

    @Test
    fun `return statistic list sorted by product count desc`() = runTest {

        Mockito.`when`(statisticRepo.getStatistic("token", "cafeUuid", period = "1"))
            .thenReturn(
                ApiResult.Success(
                    statisticList
                )
            )

        Mockito.`when`(dataStoreRepo.token)
            .thenReturn(
                flow {
                    emit("token")
                }
            )

        Assert.assertEquals(
            ApiResult.Success(statisticListSortedByProductCountDesc),
            getStatisticUseCase.invoke(
                "cafeUuid",
                period = "1",
                true,
                GetStatisticUseCase.StatisticSortOption.BY_PRODUCT_COUNT
            )
        )
    }

    @Test
    fun `return statistic list sorted by product count asc`() = runTest {

        Mockito.`when`(statisticRepo.getStatistic("token", "cafeUuid", period = "1"))
            .thenReturn(
                ApiResult.Success(
                    statisticList
                )
            )

        Mockito.`when`(dataStoreRepo.token)
            .thenReturn(
                flow {
                    emit("token")
                }
            )

        Assert.assertEquals(
            ApiResult.Success(statisticListSortedByProductCountAsc),
            getStatisticUseCase.invoke(
                "cafeUuid",
                period = "1",
                false,
                GetStatisticUseCase.StatisticSortOption.BY_PRODUCT_COUNT
            )
        )
    }

    @Test
    fun `return statistic list sorted by proceed count desc`() = runTest {

        Mockito.`when`(statisticRepo.getStatistic("token", "cafeUuid", period = "1"))
            .thenReturn(
                ApiResult.Success(
                    statisticList
                )
            )

        Mockito.`when`(dataStoreRepo.token)
            .thenReturn(
                flow {
                    emit("token")
                }
            )

        Assert.assertEquals(
            ApiResult.Success(statisticListSortedByProceedCountDesc),
            getStatisticUseCase.invoke(
                "cafeUuid",
                period = "1",
                true,
                GetStatisticUseCase.StatisticSortOption.BY_PROCEEDS
            )
        )
    }

    @Test
    fun `return statistic list sorted by proceed count asc`() = runTest {

        Mockito.`when`(statisticRepo.getStatistic("token", "cafeUuid", period = "1"))
            .thenReturn(
                ApiResult.Success(
                    statisticList
                )
            )

        Mockito.`when`(dataStoreRepo.token)
            .thenReturn(
                flow {
                    emit("token")
                }
            )

        Assert.assertEquals(
            ApiResult.Success(statisticListSortedByProceedCountAsc),
            getStatisticUseCase.invoke(
                "cafeUuid",
                period = "1",
                false,
                GetStatisticUseCase.StatisticSortOption.BY_PROCEEDS
            )
        )
    }


    companion object {
        val statisticList = listOf(
            Statistic(
                period = "1",
                startPeriodTime = 1,
                orderCount = 2,
                proceeds = 200,
                averageCheck = 250,
                productStatisticList = listOf(
                    ProductStatistic(
                        name = "Burger",
                        orderCount = 200,
                        productCount = 2,
                        proceeds = 20,
                    ),
                    ProductStatistic(
                        name = "Kartoxa",
                        orderCount = 100,
                        productCount = 1,
                        proceeds = 10,
                    ),
                    ProductStatistic(
                        name = "Pizza",
                        orderCount = 300,
                        productCount = 3,
                        proceeds = 30,
                    )
                )
            )
        )

        val statisticListSortedByOrderCountDesc = listOf(
            Statistic(
                period = "1",
                startPeriodTime = 1,
                orderCount = 2,
                proceeds = 200,
                averageCheck = 250,
                productStatisticList = listOf(
                    ProductStatistic(
                        name = "Pizza",
                        orderCount = 300,
                        productCount = 3,
                        proceeds = 30,
                    ),
                    ProductStatistic(
                        name = "Burger",
                        orderCount = 200,
                        productCount = 2,
                        proceeds = 20,
                    ),
                    ProductStatistic(
                        name = "Kartoxa",
                        orderCount = 100,
                        productCount = 1,
                        proceeds = 10,
                    ),
                )
            )
        )

        val statisticListSortedByOrderCountAsc = listOf(
            Statistic(
                period = "1",
                startPeriodTime = 1,
                orderCount = 2,
                proceeds = 200,
                averageCheck = 250,
                productStatisticList = listOf(
                    ProductStatistic(
                        name = "Kartoxa",
                        orderCount = 100,
                        productCount = 1,
                        proceeds = 10,
                    ),
                    ProductStatistic(
                        name = "Burger",
                        orderCount = 200,
                        productCount = 2,
                        proceeds = 20,
                    ),
                    ProductStatistic(
                        name = "Pizza",
                        orderCount = 300,
                        productCount = 3,
                        proceeds = 30,
                    ),
                )
            )
        )

        val statisticListSortedByProductCountDesc = listOf(
            Statistic(
                period = "1",
                startPeriodTime = 1,
                orderCount = 2,
                proceeds = 200,
                averageCheck = 250,
                productStatisticList = listOf(
                    ProductStatistic(
                        name = "Pizza",
                        orderCount = 300,
                        productCount = 3,
                        proceeds = 30,
                    ),
                    ProductStatistic(
                        name = "Burger",
                        orderCount = 200,
                        productCount = 2,
                        proceeds = 20,
                    ),
                    ProductStatistic(
                        name = "Kartoxa",
                        orderCount = 100,
                        productCount = 1,
                        proceeds = 10,
                    ),
                )
            )
        )
        val statisticListSortedByProductCountAsc = listOf(
            Statistic(
                period = "1",
                startPeriodTime = 1,
                orderCount = 2,
                proceeds = 200,
                averageCheck = 250,
                productStatisticList = listOf(
                    ProductStatistic(
                        name = "Kartoxa",
                        orderCount = 100,
                        productCount = 1,
                        proceeds = 10,
                    ),
                    ProductStatistic(
                        name = "Burger",
                        orderCount = 200,
                        productCount = 2,
                        proceeds = 20,
                    ),
                    ProductStatistic(
                        name = "Pizza",
                        orderCount = 300,
                        productCount = 3,
                        proceeds = 30,
                    ),
                )
            )
        )

        val statisticListSortedByProceedCountDesc = listOf(
            Statistic(
                period = "1",
                startPeriodTime = 1,
                orderCount = 2,
                proceeds = 200,
                averageCheck = 250,
                productStatisticList = listOf(
                    ProductStatistic(
                        name = "Pizza",
                        orderCount = 300,
                        productCount = 3,
                        proceeds = 30,
                    ),
                    ProductStatistic(
                        name = "Burger",
                        orderCount = 200,
                        productCount = 2,
                        proceeds = 20,
                    ),
                    ProductStatistic(
                        name = "Kartoxa",
                        orderCount = 100,
                        productCount = 1,
                        proceeds = 10,
                    ),
                )
            )
        )
        val statisticListSortedByProceedCountAsc = listOf(
            Statistic(
                period = "1",
                startPeriodTime = 1,
                orderCount = 2,
                proceeds = 200,
                averageCheck = 250,
                productStatisticList = listOf(
                    ProductStatistic(
                        name = "Kartoxa",
                        orderCount = 100,
                        productCount = 1,
                        proceeds = 10,
                    ),
                    ProductStatistic(
                        name = "Burger",
                        orderCount = 200,
                        productCount = 2,
                        proceeds = 20,
                    ),
                    ProductStatistic(
                        name = "Pizza",
                        orderCount = 300,
                        productCount = 3,
                        proceeds = 30,
                    ),
                )
            )
        )
    }


}