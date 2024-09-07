package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.CalculateImageCompressQualityUseCase
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.domain.feature.menu.createmenuproduct.CreateMenuProductUseCase
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
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
    private val calculateImageCompressQualityUseCase: CalculateImageCompressQualityUseCase = mockk()
    private lateinit var createMenuProductUseCase: CreateMenuProductUseCase

    @BeforeTest
    fun setup() {
        createMenuProductUseCase = CreateMenuProductUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
            photoRepo = photoRepo,
            getUsernameUseCase = getUsernameUseCase,
            calculateImageCompressQualityUseCase = calculateImageCompressQualityUseCase
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
            selectedCategories = categoriesMock
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
        coEvery { getUsernameUseCase() } returns username
        coEvery { photoRepo.getFileSizeInMb(imageUri) } returns 250
        coEvery { calculateImageCompressQualityUseCase(250) } returns 40
        coEvery {
            photoRepo.uploadPhoto(
                uri = imageUri,
                compressQuality = 40,
                username = username
            )
        } returns Photo(url = photoUrl)
        coEvery { dataStoreRepo.getToken() } returns token

        // When
        createMenuProductUseCase.invoke(params)

        // Then
        coVerify { getUsernameUseCase() }
        coVerify { dataStoreRepo.getToken() }
        coVerify { menuProductRepo.saveMenuProduct(token, menuProductPost) }
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        // Given
        val uri = "imageUri"
        val params = paramsMock.copy(
            name = "Pizza",
            newPrice = "1000",
            description = "Delicious pizza",
            selectedCategories = categoriesMock,
            imageUri = uri
        )
        val username = "username"
        coEvery { getUsernameUseCase() } returns username
        coEvery { photoRepo.getFileSizeInMb(uri) } returns 100
        coEvery { calculateImageCompressQualityUseCase(100) } returns 100
        coEvery {
            photoRepo.uploadPhoto(
                uri = uri,
                compressQuality = 100,
                username = username
            )
        } returns Photo(url = "url")
        coEvery { dataStoreRepo.getToken() } returns null

        // When & Then
        assertFailsWith<NoTokenException> {
            createMenuProductUseCase.invoke(params)
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
            selectedCategories = categoriesMock
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
            selectedCategories = categoriesMock
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
            selectedCategories = categoriesMock
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
            selectedCategories = emptyList()
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
        selectedCategories = emptyList()
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
