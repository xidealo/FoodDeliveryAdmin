package com.bunbeauty.domain

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.usecase.UpdateMenuProductUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateMenuProductUseCaseTest {

    private val menuProductRepo: MenuProductRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var useCase: UpdateMenuProductUseCase
    private val token = "token"
    private val menuProductUuid = "menuProductUuid"

    @BeforeTest
    fun setup() {
        useCase = UpdateMenuProductUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
        )
    }

    @Test
    fun `throw NoTokenException when token is null`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns null

        //Result
        assertFailsWith(
            exceptionClass = NoTokenException::class,
            block = { useCase(menuProductUuid, mockk()) }
        )
    }

    @Test
    fun `throw MenuProductNameException when name is empty`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns token

        //Result
        assertFailsWith(
            exceptionClass = MenuProductNameException::class,
            block = { useCase(menuProductUuid, menuProductMock.copy(name = "")) }
        )
    }

    @Test
    fun `throw MenuProductNewPriceException when newPrice is empty`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns token

        //Result
        assertFailsWith(
            exceptionClass = MenuProductNewPriceException::class,
            block = { useCase(menuProductUuid, menuProductMock.copy(newPrice = 0)) }
        )
    }

    @Test
    fun `throw MenuProductDescriptionException when description is empty`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns token

        //Result
        assertFailsWith(
            exceptionClass = MenuProductDescriptionException::class,
            block = { useCase(menuProductUuid, menuProductMock.copy(description = "")) }
        )
    }

    @Test
    fun `verify calling updateMenuProduct from menuProductRepo when data is ok`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns token

        // When
        useCase(menuProductUuid, menuProductMock)

        // Then
        coVerify {
            menuProductRepo.updateMenuProduct(
                menuProductUuid = menuProductUuid,
                updateMenuProduct = menuProductMock,
                token = token
            )
        }
    }

    private val menuProductMock = UpdateMenuProduct(
        name = "name",
        newPrice = 1,
        oldPrice = 2,
        utils = "utils",
        nutrition = 2,
        description = "description",
        comboDescription = "comboDescription",
        photoLink = "photoLink",
        isVisible = true,
        categoryUuids = emptyList(),
    )
}