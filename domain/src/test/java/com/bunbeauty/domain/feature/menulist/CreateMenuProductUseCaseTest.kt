package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.addmenuproduct.CreateMenuProductUseCase
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.category.Category
import com.bunbeauty.domain.model.category.SelectableCategory
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.PhotoRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CreateMenuProductUseCaseTest {

    private val menuProductRepo: MenuProductRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val photoRepo: PhotoRepo = mockk()
    private val getUsernameUseCase: GetUsernameUseCase = mockk()
    private lateinit var createMenuProductUseCase: CreateMenuProductUseCase

    @BeforeTest
    fun setup() {
        createMenuProductUseCase = CreateMenuProductUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
            photoRepo = photoRepo,
            getUsernameUseCase = getUsernameUseCase,
        )
    }

    @Test
    fun `invoke successfully adds menu product`() = runTest {
        // Given
        val username = "username"
        val imageUri = "uri"
        val photoUrl = "url"
        val token = "token"
        val params = paramsMock.copy(
            name = "Pizza",
            newPrice = "1000",
            description = "Delicious pizza",
            imageUri = imageUri,
            categories = categoriesMock
        )
        val menuProductPost = MenuProductPost(
            name = "Pizza",
            newPrice = 1000,
            oldPrice = null,
            utils = "",
            nutrition = null,
            description = "Delicious pizza",
            comboDescription = "",
            photoLink = photoUrl,
            isVisible = false,
            isRecommended = false,
            barcode = 0,
            categories = listOf("123")
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { getUsernameUseCase() } returns username
        coEvery {
            photoRepo.uploadPhoto(
                uri = imageUri,
                username = username
            )
        } returns Photo(photoUrl)

        // When
        createMenuProductUseCase.invoke(params)

        // Then
        coVerify { getUsernameUseCase() }
        coVerify { dataStoreRepo.getToken() }
        coVerify { menuProductRepo.post(token, menuProductPost) }
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        // Given
        val menuProductPost = paramsMock.copy(
            name = "Pizza",
            newPrice = "1000",
            description = "Delicious pizza",
            categories = categoriesMock
        )
        coEvery { dataStoreRepo.getToken() } returns null

        // When & Then
        assertFailsWith<NoTokenException> {
            createMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductNameException when name is empty`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = paramsMock.copy(
            name = "",
            newPrice = "1000",
            description = "Delicious pizza",
            categories = categoriesMock
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductNameException> {
            createMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductNewPriceException when new price is zero`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = paramsMock.copy(
            name = "Pizza",
            newPrice = "0",
            description = "Delicious pizza",
            categories = categoriesMock
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductNewPriceException> {
            createMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductDescriptionException when description is empty`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = paramsMock.copy(
            name = "Pizza",
            newPrice = "1000",
            description = "",
            categories = categoriesMock
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductDescriptionException> {
            createMenuProductUseCase.invoke(menuProductPost)
        }
    }

    @Test
    fun `invoke throws MenuProductCategoriesException when categories are empty`() = runTest {
        // Given
        val token = "valid_token"
        val menuProductPost = paramsMock.copy(
            name = "Pizza",
            newPrice = "1000",
            description = "Delicious pizza",
            categories = emptyList()
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<MenuProductCategoriesException> {
            createMenuProductUseCase.invoke(menuProductPost)
        }
    }

    private val paramsMock = CreateMenuProductUseCase.Params(
        name = "",
        newPrice = "0",
        oldPrice = "",
        utils = "",
        nutrition = "",
        description = "",
        comboDescription = "",
        imageUri = "",
        isVisible = false,
        isRecommended = false,
        categories = emptyList()
    )

    private val categoriesMock = listOf(
        SelectableCategory(
            category = Category(
                uuid = "123",
                name = "",
                priority = 0
            ),
            selected = true
        )
    )
}
