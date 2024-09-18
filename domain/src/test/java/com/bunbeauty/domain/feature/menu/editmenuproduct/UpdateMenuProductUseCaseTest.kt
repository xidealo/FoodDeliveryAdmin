package com.bunbeauty.domain.feature.menu.editmenuproduct

import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.domain.feature.menu.common.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.menu.common.photo.UploadPhotoUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductCategoriesUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductDescriptionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNameUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNewPriceUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductNutritionUseCase
import com.bunbeauty.domain.feature.menu.common.validation.ValidateMenuProductOldPriceUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.MenuProductNotUpdatedException
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateMenuProductUseCaseTest {

    private val uuid = "uuid"
    private val name = "name"
    private val newPrice = "100"
    private val newPriceInt = 100
    private val oldPrice = ""
    private val oldPriceInt = 0
    private val nutrition = "200"
    private val nutritionInt = 200
    private val units = "г"
    private val description = "description"
    private val imageUri = "uri"
    private val photoLinkInitial = "linkInitial"
    private val photoLink = "link"
    private val tokenMock = "token"
    private val categories = listOf(
        SelectableCategory(
            category = Category(
                uuid = "123",
                name = "",
                priority = 0
            ),
            selected = true
        )
    )

    private val validateMenuProductNameUseCase: ValidateMenuProductNameUseCase = mockk {
        every { this@mockk.invoke(name) } returns name
    }
    private val validateMenuProductNewPriceUseCase: ValidateMenuProductNewPriceUseCase = mockk {
        every { this@mockk.invoke(newPrice) } returns newPriceInt
    }
    private val validateMenuProductOldPriceUseCase: ValidateMenuProductOldPriceUseCase = mockk {
        every { this@mockk.invoke(oldPrice = oldPrice, newPrice = newPriceInt) } returns oldPriceInt
    }
    private val validateMenuProductNutritionUseCase: ValidateMenuProductNutritionUseCase = mockk {
        every { this@mockk.invoke(nutrition = nutrition, units = units) } returns 200
    }
    private val validateMenuProductDescriptionUseCase: ValidateMenuProductDescriptionUseCase = mockk {
        every { this@mockk.invoke(description = description) } returns description
    }
    private val validateMenuProductCategoriesUseCase: ValidateMenuProductCategoriesUseCase = mockk {
        every { this@mockk.invoke(categories = categories) } returns categories
    }
    private val uploadPhotoUseCase: UploadPhotoUseCase = mockk {
        coEvery { this@mockk.invoke(imageUri = imageUri) } returns Photo(url = photoLink)
    }
    private val deletePhotoUseCase: DeletePhotoUseCase = mockk {
        coEvery { this@mockk.invoke(photoLink = photoLinkInitial) } returns Unit
    }
    private val menuProductRepo: MenuProductRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk {
        coEvery { getToken() } returns tokenMock
    }
    private lateinit var updateMenuProductUseCase: UpdateMenuProductUseCase

    @BeforeTest
    fun setup() {
        updateMenuProductUseCase = UpdateMenuProductUseCase(
            validateMenuProductNameUseCase = validateMenuProductNameUseCase,
            validateMenuProductNewPriceUseCase = validateMenuProductNewPriceUseCase,
            validateMenuProductOldPriceUseCase = validateMenuProductOldPriceUseCase,
            validateMenuProductNutritionUseCase = validateMenuProductNutritionUseCase,
            validateMenuProductDescriptionUseCase = validateMenuProductDescriptionUseCase,
            validateMenuProductCategoriesUseCase = validateMenuProductCategoriesUseCase,
            uploadPhotoUseCase = uploadPhotoUseCase,
            deletePhotoUseCase = deletePhotoUseCase,
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `throw MenuProductNotUpdatedException when updating failed`() = runTest {
        coEvery {
            menuProductRepo.updateMenuProduct(
                menuProductUuid = uuid,
                token = tokenMock,
                updateMenuProduct = UpdateMenuProduct(
                    name = name,
                    newPrice = newPriceInt,
                    oldPrice = oldPriceInt,
                    utils = units,
                    nutrition = nutritionInt,
                    description = description,
                    comboDescription = "",
                    photoLink = null,
                    isVisible = true,
                    isRecommended = false,
                    categories = listOf("123")
                )
            )
        } returns null

        assertFailsWith<MenuProductNotUpdatedException> {
            updateMenuProductUseCase(
                UpdateMenuProductUseCase.Params(
                    uuid = uuid,
                    name = name,
                    newPrice = newPrice,
                    oldPrice = oldPrice,
                    nutrition = nutrition,
                    units = units,
                    description = description,
                    comboDescription = "",
                    selectedCategories = categories,
                    isVisible = true,
                    isRecommended = false,
                    newImageUri = null,
                    photoLink = photoLinkInitial
                )
            )
        }
    }

    @Test
    fun `return Unit when successfully updated with initial photo link`() = runTest {
        coEvery {
            menuProductRepo.updateMenuProduct(
                menuProductUuid = uuid,
                token = tokenMock,
                updateMenuProduct = UpdateMenuProduct(
                    name = name,
                    newPrice = newPriceInt,
                    oldPrice = oldPriceInt,
                    utils = units,
                    nutrition = nutritionInt,
                    description = description,
                    comboDescription = "",
                    photoLink = null,
                    isVisible = true,
                    isRecommended = false,
                    categories = listOf("123")
                )
            )
        } returns mockk()

        val result = updateMenuProductUseCase(
            UpdateMenuProductUseCase.Params(
                uuid = "uuid",
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                nutrition = nutrition,
                units = units,
                description = description,
                comboDescription = "",
                selectedCategories = categories,
                isVisible = true,
                isRecommended = false,
                newImageUri = null,
                photoLink = photoLinkInitial
            )
        )

        assertEquals(Unit, result)
    }

    @Test
    fun `return Unit when successfully updated with new photo uri`() = runTest {
        coEvery {
            menuProductRepo.updateMenuProduct(
                menuProductUuid = uuid,
                token = tokenMock,
                updateMenuProduct = UpdateMenuProduct(
                    name = name,
                    newPrice = newPriceInt,
                    oldPrice = oldPriceInt,
                    utils = units,
                    nutrition = nutritionInt,
                    description = description,
                    comboDescription = "",
                    photoLink = photoLink,
                    isVisible = true,
                    isRecommended = false,
                    categories = listOf("123")
                )
            )
        } returns mockk()

        val result = updateMenuProductUseCase(
            UpdateMenuProductUseCase.Params(
                uuid = "uuid",
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                nutrition = nutrition,
                units = units,
                description = description,
                comboDescription = "",
                selectedCategories = categories,
                isVisible = true,
                isRecommended = false,
                newImageUri = imageUri,
                photoLink = null
            )
        )

        assertEquals(Unit, result)
    }
}
