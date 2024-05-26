package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.addmenuproduct.AddMenuProductUseCase
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AddMenuProductUseCaseTest {

    private val menuProductRepo: MenuProductRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var addMenuProductUseCase: AddMenuProductUseCase

    @BeforeTest
    fun setup() {
        addMenuProductUseCase = AddMenuProductUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke successfully adds menu product`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = menuProductPostMock.copy(
            name = "Pizza",
            newPrice = 1000,
            description = "Delicious pizza",
            categories = listOf("Food")
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { menuProductRepo.post(token, menuProductPost) } returns Unit

        // When
        addMenuProductUseCase.invoke(menuProductPost)

        // Then
        coVerify { dataStoreRepo.getToken() }
        coVerify { menuProductRepo.post(token, menuProductPost) }
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        // Given
        val menuProductPost = menuProductPostMock.copy(
            name = "Pizza",
            newPrice = 1000,
            description = "Delicious pizza",
            categories = listOf("Food")
        )
        coEvery { dataStoreRepo.getToken() } returns null

        // When & Then
        assertFailsWith<NoTokenException> {
            addMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductNameException when name is empty`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = menuProductPostMock.copy(
            name = "",
            newPrice = 1000,
            description = "Delicious pizza",
            categories = listOf("Food")
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductNameException> {
            addMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductNewPriceException when new price is zero`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = menuProductPostMock.copy(
            name = "Pizza",
            newPrice = 0,
            description = "Delicious pizza",
            categories = listOf("Food")
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductNewPriceException> {
            addMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductDescriptionException when description is empty`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = menuProductPostMock.copy(
            name = "Pizza",
            newPrice = 1000,
            description = "",
            categories = listOf("Food")
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductDescriptionException> {
            addMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductCategoriesException when categories are empty`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = menuProductPostMock.copy(
            name = "Pizza",
            newPrice = 1000,
            description = "Delicious pizza",
            categories = emptyList()
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductCategoriesException> {
            addMenuProductUseCase.invoke(menuProductPost)
        }
    }

    private val menuProductPostMock = MenuProductPost(
        name = "",
        newPrice = 0,
        oldPrice = null,
        utils = null,
        nutrition = null,
        description = "",
        comboDescription = null,
        photoLink = "",
        barcode = 0,
        isVisible = false,
        isRecommended = false,
        categories = listOf()
    )
}
