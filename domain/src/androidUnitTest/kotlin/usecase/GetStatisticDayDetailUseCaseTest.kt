package usecase

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.model.statistic.StatisticDayProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import com.bunbeauty.domain.usecase.GetStatisticDayDetailUseCase
import common.Constants.RUBLE_CURRENCY
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetStatisticDayDetailUseCaseTest {
    private val statisticRepo: StatisticRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var useCase: GetStatisticDayDetailUseCase

    @BeforeTest
    fun setup() {
        useCase =
            GetStatisticDayDetailUseCase(
                statisticRepo = statisticRepo,
                dataStoreRepo = dataStoreRepo,
            )
    }

    @Test
    fun `invoke returns statistic day detail from repo`() =
        runTest {
            val date = "2026-05-09"
            val token = "token"
            val companyUuid = "company-uuid"
            val expected =
                StatisticDayDetail(
                    companyUuid = companyUuid,
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
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
            coEvery {
                statisticRepo.getStatisticDayDetail(
                    token = token,
                    companyUuid = companyUuid,
                    date = date,
                )
            } returns expected

            val result = useCase(date)

            assertEquals(expected, result)
            coVerify(exactly = 1) {
                statisticRepo.getStatisticDayDetail(
                    token = token,
                    companyUuid = companyUuid,
                    date = date,
                )
            }
        }

    @Test
    fun `invoke throws NoTokenException when token missing`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                useCase("2026-05-09")
            }
        }

    @Test
    fun `invoke throws NoCompanyUuidException when company uuid missing`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns "t"
            coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

            assertFailsWith<NoCompanyUuidException> {
                useCase("2026-05-09")
            }
        }
}
