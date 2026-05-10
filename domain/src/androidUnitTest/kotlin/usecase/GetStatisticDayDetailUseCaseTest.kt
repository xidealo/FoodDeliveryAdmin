package usecase

import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.model.statistic.StatisticDayProduct
import com.bunbeauty.domain.repo.StatisticRepo
import com.bunbeauty.domain.usecase.GetStatisticDayDetailUseCase
import common.Constants.RUBLE_CURRENCY
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetStatisticDayDetailUseCaseTest {
    private val statisticRepo: StatisticRepo = mockk()
    private lateinit var useCase: GetStatisticDayDetailUseCase

    @BeforeTest
    fun setup() {
        useCase =
            GetStatisticDayDetailUseCase(
                statisticRepo = statisticRepo,
            )
    }

    @Test
    fun `invoke delegates to statisticRepo with date`() =
        runTest {
            val date = "2026-05-09"
            val expected =
                StatisticDayDetail(
                    companyUuid = "company-uuid",
                    date = date,
                    orderCount = 1,
                    orderProceedsTotal = 100,
                    orderProceedsProducts = 90,
                    averageCheck = 100.0,
                    deliveryOrderCount = 0,
                    pickupOrderCount = 1,
                    products =
                        listOf(
                            StatisticDayProduct(
                                menuProductUuid = "p1",
                                name = "Burger",
                                photoLink = "",
                                productCount = 2,
                                proceeds = 90,
                                currency = RUBLE_CURRENCY,
                            ),
                        ),
                    currency = RUBLE_CURRENCY,
                )
            coEvery {
                statisticRepo.getStatisticDayDetail(date)
            } returns expected

            val result = useCase(date)

            assertEquals(expected, result)
            coVerify(exactly = 1) {
                statisticRepo.getStatisticDayDetail(date)
            }
        }
}
