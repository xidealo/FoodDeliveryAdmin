package com.bunbeauty.domain

import com.bunbeauty.domain.exception.updateproduct.NotFoundMenuProductException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.usecase.GetMenuProductUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetMenuProductUseCaseTest {

    private val menuProductRepo: MenuProductRepo = mockk()
    private lateinit var useCase: GetMenuProductUseCase

    @BeforeTest
    fun setup() {
        useCase = GetMenuProductUseCase(
            menuProductRepo = menuProductRepo
        )
    }

    @Test
    fun `throw NotFoundMenuProductException when menu product is null`() = runTest {
        // Given
        coEvery { menuProductRepo.getMenuProduct("uuid") } returns null

        // Result
        assertFailsWith(
            exceptionClass = NotFoundMenuProductException::class,
            block = { useCase("uuid") }
        )
    }

    @Test
    fun `return menu product when menu product is not null`() = runTest {
        // Given
        coEvery { menuProductRepo.getMenuProduct("uuid") } returns menuProductMock

        // When
        val result = useCase("uuid")

        // Then
        assertEquals(menuProductMock, result)
    }

    private val menuProductMock = MenuProduct(
        uuid = "uuid",
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
        categories = emptyList()
    )
}
