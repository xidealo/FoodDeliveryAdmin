package com.bunbeauty.domain

import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.usecase.GetSeparatedMenuProductListUseCase
import com.bunbeauty.domain.usecase.SeparatedMenuProductList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSeparatedMenuProductListUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val menuProductRepo: MenuProductRepo = mockk()
    private lateinit var useCase: GetSeparatedMenuProductListUseCase

    @BeforeTest
    fun setup() {
        useCase = GetSeparatedMenuProductListUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `return empty lists when menuProductRepo return empty list`() = runTest {
        // Given
        val companyUuid = "companyUuid"
        val isRefreshing = false
        val expectedSeparatedMenuProductList = SeparatedMenuProductList(
            visibleList = emptyList(),
            hiddenList = emptyList()
        )
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
        assertEquals(expectedSeparatedMenuProductList, separatedMenuProductList)
    }

    @Test
    fun `return empty visible products when menuProductRepo return list with empty isVisible`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            val expectedSeparatedMenuProductList = SeparatedMenuProductList(
                visibleList = emptyList(),
                hiddenList = listOf(
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
                    )
                )
            )
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
                )
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertEquals(expectedSeparatedMenuProductList, separatedMenuProductList)
        }

    @Test
    fun `return empty hidden products when menuProductRepo return list with full isVisible`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            val expectedSeparatedMenuProductList = SeparatedMenuProductList(
                visibleList = listOf(
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
                    )
                ),
                hiddenList = emptyList()
            )

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
                )
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertEquals(expectedSeparatedMenuProductList, separatedMenuProductList)
        }

    @Test
    fun `return 2 hidden 3 visible products when menuProductRepo has same data by visible`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            val expectedSeparatedMenuProductList = SeparatedMenuProductList(
                visibleList = listOf(
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
                    )
                ),
                hiddenList = listOf(
                    menuProductMock.copy(
                        uuid = "uuid1",
                        isVisible = false
                    ),
                    menuProductMock.copy(
                        uuid = "uuid2",
                        isVisible = false
                    )
                )
            )
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
                )
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertEquals(expectedSeparatedMenuProductList, separatedMenuProductList)
        }

    @Test
    fun `return sorted by name started with A and finished with Z when menuProductRepo has not empty list`() =
        runTest {
            val companyUuid = "companyUuid"
            val isRefreshing = false
            val expectedSeparatedMenuProductList = SeparatedMenuProductList(
                visibleList = listOf(
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
                    )
                ),
                hiddenList = emptyList()
            )
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
                    uuid = "uuid4",
                    name = "C"
                ),
                menuProductMock.copy(
                    uuid = "uuid3",
                    name = "B"
                ),
                menuProductMock.copy(
                    uuid = "uuid5",
                    name = "Z"
                )
            )
            // When
            val separatedMenuProductList = useCase(isRefreshing)
            // Then
            assertEquals(expectedSeparatedMenuProductList, separatedMenuProductList)
        }

    private val menuProductMock = MenuProduct(
        uuid = "productUuidMock",
        name = "name",
        newPrice = 1,
        oldPrice = 2,
        units = "utils",
        nutrition = 2,
        description = "description",
        comboDescription = "comboDescription",
        photoLink = "photoLink",
        barcode = 2,
        isVisible = true,
        isRecommended = true,
        categoryUuids = emptyList(),
        additionGroups = emptyList()
    )
}
