package com.bunbeauty.domain

import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.usecase.GetSeparatedMenuProductListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetSeparatedAdditionListUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val menuProductRepo: MenuProductRepo = mockk()
    private lateinit var useCase: GetSeparatedMenuProductListUseCase

    @BeforeTest
    fun setup() {
        useCase = GetSeparatedMenuProductListUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
        )
    }

    @Test
    fun `return empty lists when menuProductRepo return empty list`() = runTest {
        // Given
        val companyUuid = "companyUuid"
        val isRefreshing = false
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            menuProductRepo.getMenuProductList(
                companyUuid = companyUuid,
                takeRemote = isRefreshing
            )
        } returns emptyList()

        // When
        val separatedMenuProductList = useCase(isRefreshing)
        // Then
        assertTrue(separatedMenuProductList.visibleList.isEmpty())
        assertTrue(separatedMenuProductList.hiddenList.isEmpty())
    }

    @Test
    fun `return empty visible products when menuProductRepo return list with empty isVisible`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
            coEvery {
                menuProductRepo.getMenuProductList(
                    companyUuid = companyUuid,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                menuProductMock.copy(
                    uuid = "uuid1",
                    isVisible = false
                ),
                menuProductMock.copy(
                    uuid = "uuid2",
                    isVisible = false
                ),
                menuProductMock.copy(
                    uuid = "uuid3",
                    isVisible = false
                ),
                menuProductMock.copy(
                    uuid = "uuid4",
                    isVisible = false
                ),
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertTrue(separatedMenuProductList.visibleList.isEmpty())
            assertTrue(separatedMenuProductList.hiddenList.isNotEmpty())
        }

    @Test
    fun `return empty hidden products when menuProductRepo return list with full isVisible`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
            coEvery {
                menuProductRepo.getMenuProductList(
                    companyUuid = companyUuid,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                menuProductMock.copy(
                    uuid = "uuid1",
                    isVisible = true
                ),
                menuProductMock.copy(
                    uuid = "uuid2",
                    isVisible = true
                ),
                menuProductMock.copy(
                    uuid = "uuid3",
                    isVisible = true
                ),
                menuProductMock.copy(
                    uuid = "uuid4",
                    isVisible = true
                ),
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertTrue(separatedMenuProductList.visibleList.isNotEmpty())
            assertTrue(separatedMenuProductList.hiddenList.isEmpty())
        }

    @Test
    fun `return 2 hidden 3 visible products when menuProductRepo has same data by visible`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
            coEvery {
                menuProductRepo.getMenuProductList(
                    companyUuid = companyUuid,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                menuProductMock.copy(
                    uuid = "uuid1",
                    isVisible = false
                ),
                menuProductMock.copy(
                    uuid = "uuid2",
                    isVisible = false
                ),
                menuProductMock.copy(
                    uuid = "uuid3",
                    isVisible = true
                ),
                menuProductMock.copy(
                    uuid = "uuid4",
                    isVisible = true
                ),
                menuProductMock.copy(
                    uuid = "uuid5",
                    isVisible = true
                ),
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertTrue(separatedMenuProductList.visibleList.size == 3)
            assertTrue(separatedMenuProductList.hiddenList.size == 2)
        }


    @Test
    fun `return sorted by name started with A and finished with Z when menuProductRepo has not empty list`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
            coEvery {
                menuProductRepo.getMenuProductList(
                    companyUuid = companyUuid,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                menuProductMock.copy(
                    uuid = "uuid2",
                    name = "A"
                ),
                menuProductMock.copy(
                    uuid = "uuid3",
                    name = "B"
                ),
                menuProductMock.copy(
                    uuid = "uuid4",
                    name = "C"
                ),
                menuProductMock.copy(
                    uuid = "uuid5",
                    name = "Z"
                ),
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertEquals(separatedMenuProductList.visibleList.first().name, "A")
            assertEquals(separatedMenuProductList.visibleList.last().name, "Z")
        }

    private val menuProductMock = MenuProduct(
        uuid = "productUuidMock",
        name = "name",
        newPrice = 1,
        oldPrice = 2,
        utils = "utils",
        nutrition = 2,
        description = "description",
        comboDescription = "comboDescription",
        photoLink = "photoLink",
        barcode = 2,
        isVisible = true,
        categories = emptyList(),
    )
}