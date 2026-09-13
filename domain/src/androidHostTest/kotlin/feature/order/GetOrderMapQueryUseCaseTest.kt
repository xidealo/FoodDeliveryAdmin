package test.feature.order

import com.bunbeauty.domain.feature.order.usecase.GetOrderMapQueryUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.city.City
import com.bunbeauty.domain.model.order.details.OrderAddress
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetOrderMapQueryUseCaseTest {
    private val cafeRepo: CafeRepo = mockk()
    private val cityRepo: CityRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var getOrderMapQueryUseCase: GetOrderMapQueryUseCase

    @BeforeTest
    fun setUp() {
        getOrderMapQueryUseCase =
            GetOrderMapQueryUseCase(
                cafeRepo = cafeRepo,
                cityRepo = cityRepo,
                dataStoreRepo = dataStoreRepo,
            )
    }

    @Test
    fun `invoke() should return city street and house for delivery`() =
        runTest {
            val cafe = cafe()
            val city = city(name = "Москва")
            coEvery { cafeRepo.getCafeByUuid(CAFE_UUID) } returns cafe
            coEvery { dataStoreRepo.companyUuid } returns flowOf(COMPANY_UUID)
            coEvery {
                cityRepo.getCityByUuid(
                    companyUuid = COMPANY_UUID,
                    cityUuid = CITY_UUID,
                )
            } returns city

            val result =
                getOrderMapQueryUseCase(
                    orderDetails(
                        isDelivery = true,
                        address =
                            OrderAddress(
                                description = null,
                                street = "улица Ленина",
                                house = "5",
                                flat = "10",
                                entrance = "2",
                                floor = "3",
                                comment = "домофон",
                            ),
                    ),
                )

            assertEquals("Москва, улица Ленина, 5", result)
        }

    @Test
    fun `invoke() should return city and description when description is present`() =
        runTest {
            val cafe = cafe()
            val city = city(name = "Москва")
            coEvery { cafeRepo.getCafeByUuid(CAFE_UUID) } returns cafe
            coEvery { dataStoreRepo.companyUuid } returns flowOf(COMPANY_UUID)
            coEvery {
                cityRepo.getCityByUuid(
                    companyUuid = COMPANY_UUID,
                    cityUuid = CITY_UUID,
                )
            } returns city

            val result =
                getOrderMapQueryUseCase(
                    orderDetails(
                        isDelivery = true,
                        address =
                            OrderAddress(
                                description = "ЖК Солнечный, корпус 2",
                                street = "улица Ленина",
                                house = "5",
                                flat = null,
                                entrance = null,
                                floor = null,
                                comment = null,
                            ),
                    ),
                )

            assertEquals("Москва, ЖК Солнечный, корпус 2", result)
        }

    @Test
    fun `invoke() should return null for pickup`() =
        runTest {
            val result =
                getOrderMapQueryUseCase(
                    orderDetails(
                        isDelivery = false,
                        address =
                            OrderAddress(
                                description = null,
                                street = "улица Ленина",
                                house = "5",
                                flat = null,
                                entrance = null,
                                floor = null,
                                comment = null,
                            ),
                    ),
                )

            assertNull(result)
        }

    @Test
    fun `invoke() should return null when street and house are empty`() =
        runTest {
            val result =
                getOrderMapQueryUseCase(
                    orderDetails(
                        isDelivery = true,
                        address =
                            OrderAddress(
                                description = null,
                                street = null,
                                house = null,
                                flat = "10",
                                entrance = null,
                                floor = null,
                                comment = null,
                            ),
                    ),
                )

            assertNull(result)
        }

    @Test
    fun `invoke() should return address without city when city is missing`() =
        runTest {
            coEvery { cafeRepo.getCafeByUuid(CAFE_UUID) } returns null

            val result =
                getOrderMapQueryUseCase(
                    orderDetails(
                        isDelivery = true,
                        address =
                            OrderAddress(
                                description = null,
                                street = "улица Ленина",
                                house = "5",
                                flat = null,
                                entrance = null,
                                floor = null,
                                comment = null,
                            ),
                    ),
                )

            assertEquals("улица Ленина, 5", result)
        }

    private fun orderDetails(
        isDelivery: Boolean,
        address: OrderAddress,
    ): OrderDetails =
        OrderDetails.mock.copy(
            isDelivery = isDelivery,
            address = address,
            cafeUuid = CAFE_UUID,
        )

    private fun cafe(): Cafe =
        Cafe(
            uuid = CAFE_UUID,
            address = "address",
            latitude = 0.0,
            longitude = 0.0,
            fromTime = 0,
            toTime = 0,
            offset = 0,
            phone = "123",
            visible = true,
            additional = false,
            cityUuid = CITY_UUID,
            workload = WorkLoad.LOW,
            workType = WorkType.DELIVERY,
        )

    private fun city(name: String): City =
        City(
            uuid = CITY_UUID,
            name = name,
            timeZone = "UTC+3",
            isVisible = true,
        )

    private companion object {
        const val CAFE_UUID = "cafeUuid"
        const val CITY_UUID = "cityUuid"
        const val COMPANY_UUID = "companyUuid"
    }
}
