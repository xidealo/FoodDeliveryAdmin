package com.bunbeauty.domain.feature.menu.editmenuproduct

import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.NotFoundMenuProductException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetMenuProductUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val menuProductRepo: MenuProductRepo = mockk()
    private lateinit var getMenuProductUseCase: GetMenuProductUseCase

    @BeforeTest
    fun setup() {
        getMenuProductUseCase = GetMenuProductUseCase(
            dataStoreRepo = dataStoreRepo,
            menuProductRepo = menuProductRepo
        )
    }

    @Test
    fun `throw NotFoundMenuProductException when menu product is null`() = runTest {
        // Given
        val menuProductUuid = "menuProductUuid"
        val companyUuid = "companyUuid"
        every { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            menuProductRepo.getMenuProduct(
                menuProductUuid = menuProductUuid,
                companyUuid = companyUuid
            )
        } returns null

        // Result
        assertFailsWith(
            exceptionClass = NotFoundMenuProductException::class,
            block = {
                getMenuProductUseCase(menuProductUuid = menuProductUuid)
            }
        )
    }

    @Test
    fun `return menu product when menu product is not null`() = runTest {
        // Given
        val menuProductUuid = "menuProductUuid"
        val companyUuid = "companyUuid"
        every { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            menuProductRepo.getMenuProduct(
                menuProductUuid = menuProductUuid,
                companyUuid = companyUuid
            )
        } returns menuProductMock

        // When
        val result = getMenuProductUseCase(menuProductUuid = menuProductUuid)

        // Then
        assertEquals(menuProductMock, result)
    }

    private val menuProductMock = MenuProduct(
        uuid = "uuid",
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
        categoryUuids = emptyList()
    )
}
