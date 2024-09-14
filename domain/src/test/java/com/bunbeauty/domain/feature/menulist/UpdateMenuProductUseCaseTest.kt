package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.photo.CalculateImageCompressQualityUseCase
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.domain.feature.menu.editmenuproduct.UpdateMenuProductUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.MenuProductNotUpdatedException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
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

class UpdateMenuProductUseCaseTest {

    private val tokenMock = "token"
    private val menuProductUuid = "menuProductUuid"

    private val menuProductRepo: MenuProductRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk {
        coEvery { getToken() } returns tokenMock
    }
    private val photoRepo: PhotoRepo = mockk()
    private val getUsernameUseCase: GetUsernameUseCase = mockk()
    private val calculateImageCompressQualityUseCase: CalculateImageCompressQualityUseCase = mockk()
    private lateinit var updateMenuProductUseCase: UpdateMenuProductUseCase

    @BeforeTest
    fun setup() {
        updateMenuProductUseCase = UpdateMenuProductUseCase(
            photoRepo = photoRepo,
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
            getUsernameUseCase = getUsernameUseCase,
            calculateImageCompressQualityUseCase = calculateImageCompressQualityUseCase
        )
    }

    @Test
    fun `throw MenuProductNameException when name is empty`() = runTest {
        assertFailsWith(
            exceptionClass = MenuProductNameException::class,
            block = {
                updateMenuProductUseCase(
                    params = testParams.copy(name = "")
                )
            }
        )
    }

    @Test
    fun `throw MenuProductNewPriceException when newPrice is empty`() = runTest {
        assertFailsWith(
            exceptionClass = MenuProductNewPriceException::class,
            block = {
                updateMenuProductUseCase(
                    params = testParams.copy(newPrice = "")
                )
            }
        )
    }

    @Test
    fun `throw MenuProductOldPriceException when oldPrice is less than newPrice`() = runTest {
        assertFailsWith(
            exceptionClass = MenuProductOldPriceException::class,
            block = {
                updateMenuProductUseCase(
                    params = testParams.copy(oldPrice = "99")
                )
            }
        )
    }

    @Test
    fun `throw MenuProductDescriptionException when description is empty`() = runTest {
        assertFailsWith(
            exceptionClass = MenuProductDescriptionException::class,
            block = {
                updateMenuProductUseCase(
                    params = testParams.copy(description = "")
                )
            }
        )
    }

    @Test
    fun `throw MenuProductImageException when photoLink is null and imageUri is null`() = runTest {
        assertFailsWith(
            exceptionClass = MenuProductImageException::class,
            block = {
                updateMenuProductUseCase(
                    params = testParams.copy(
                        photoLink = null,
                        imageUri = null
                    )
                )
            }
        )
    }

    @Test
    fun `throw MenuProductUploadingImageException when photo uploading failed`() = runTest {
        // Given
        val imageUri = "imageUri"
        val username = "username"
        coEvery {
            photoRepo.getFileSizeInMb(uri = imageUri)
        } returns 200
        coEvery {
            calculateImageCompressQualityUseCase(fileSize = any())
        } returns 50
        coEvery {
            getUsernameUseCase()
        } returns username
        coEvery {
            photoRepo.uploadPhoto(
                uri = imageUri,
                compressQuality = any(),
                username = any()
            )
        } returns null

        // Result
        assertFailsWith(
            exceptionClass = MenuProductUploadingImageException::class,
            block = {
                updateMenuProductUseCase(
                    params = testParams.copy(
                        photoLink = null,
                        imageUri = imageUri
                    )
                )
            }
        )
        coVerify {
            photoRepo.uploadPhoto(
                uri = imageUri,
                compressQuality = 50,
                username = username
            )
        }
    }

    @Test
    fun `verify calling updateMenuProduct from menuProductRepo when data is ok`() = runTest {
        // Given
        val menuProductMock: MenuProduct = mockk()
        coEvery {
            menuProductRepo.updateMenuProduct(
                menuProductUuid = any(),
                updateMenuProduct = any(),
                token = any()
            )
        } returns menuProductMock

        // When
        updateMenuProductUseCase(params = testParams)

        // Then
        coVerify {
            menuProductRepo.updateMenuProduct(
                menuProductUuid = menuProductUuid,
                updateMenuProduct = UpdateMenuProduct(
                    name = "name",
                    newPrice = 100,
                    oldPrice = 0,
                    utils = "",
                    nutrition = null,
                    description = "description",
                    comboDescription = "comboDescription",
                    photoLink = null,
                    isVisible = true,
                    isRecommended = false,
                    categories = listOf("1")
                ),
                token = tokenMock
            )
        }
    }

    @Test
    fun `throw MenuProductNotUpdatedException when updating failed`() = runTest {
        // Given
        coEvery {
            menuProductRepo.updateMenuProduct(
                menuProductUuid = any(),
                updateMenuProduct = any(),
                token = any()
            )
        } returns null

        // Result
        assertFailsWith(
            exceptionClass = MenuProductNotUpdatedException::class,
            block = {
                updateMenuProductUseCase(params = testParams)
            }
        )
    }

    @Test
    fun `throw NoTokenException when token is null`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns null

        // Result
        assertFailsWith(
            exceptionClass = NoTokenException::class,
            block = {
                updateMenuProductUseCase(params = testParams)
            }
        )
    }

    private val testParams = UpdateMenuProductUseCase.Params(
        uuid = menuProductUuid,
        name = "name",
        newPrice = "100",
        oldPrice = "",
        units = "",
        nutrition = "",
        description = "description",
        comboDescription = "comboDescription",
        selectedCategories = listOf(
            SelectableCategory(
                category = Category(
                    uuid = "1",
                    name = "Category",
                    priority = 1
                ),
                selected = true
            )
        ),
        isVisible = true,
        isRecommended = false,
        imageUri = null,
        photoLink = "photoLink"
    )
}
