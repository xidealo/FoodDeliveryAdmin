package com.bunbeauty.presentation

import app.cash.turbine.test
import com.bunbeauty.domain.feature.menu.common.GetSelectableCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.domain.feature.menu.editmenuproduct.GetMenuProductUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.UpdateMenuProductUseCase
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.presentation.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProduct
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProductViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class EditMenuProductViewModelTest {

    private val getMenuProductUseCase: GetMenuProductUseCase = mockk()
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase = mockk()
    private val updateMenuProductUseCase: UpdateMenuProductUseCase = mockk()
    private lateinit var viewModel: EditMenuProductViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = EditMenuProductViewModel(
            getMenuProductUseCase = getMenuProductUseCase,
            getSelectableCategoryListUseCase = getSelectableCategoryListUseCase,
            updateMenuProductUseCase = updateMenuProductUseCase
        )
    }

    @Test
    fun `return SUCCESS state when getMenuProductUseCase has no errors`() = runTest {
        // Given
        val selectedCategories = listOf(
            SelectableCategory(
                category = getMockCategory("1"),
                selected = true
            ),
            SelectableCategory(
                category = getMockCategory("2"),
                selected = true
            ),
            SelectableCategory(
                category = getMockCategory("3"),
                selected = false
            )
        )
        coEvery { getMenuProductUseCase(productUuid) } returns menuProductMock
        coEvery { getSelectableCategoryListUseCase(listOf("1", "2")) } returns selectedCategories
        val excepted = EditMenuProduct.DataState(
            state = EditMenuProduct.DataState.State.SUCCESS,
            productUuid = productUuid,
            productName = "name",
            nameField = TextFieldData(
                value = "name",
                isError = false
            ),
            newPriceField = TextFieldData(
                value = "1",
                isError = false
            ),
            oldPriceField = TextFieldData(
                value = "2",
                isError = false
            ),
            nutrition = "3",
            utils = "utils",
            descriptionField = TextFieldData(
                value = "description",
                isError = false
            ),
            comboDescription = "comboDescription",
            categoriesField = CategoriesFieldData(
                value = selectedCategories,
                isError = false
            ),
            isVisibleInMenu = true,
            isVisibleInRecommendations = false,
            imageField = EditMenuProduct.ImageFieldData(
                value = EditMenuProduct.MenuProductImage.PhotoLink(
                    value = "photoLink"
                ),
                isError = false
            ),
            sendingToServer = false
        )

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.onAction(
                EditMenuProduct.Action.LoadData(productUuid = productUuid)
            )

            // Then
            assertEquals(
                expected = excepted,
                actual = awaitItem()
            )
        }
    }

    private val productUuid: String = "1"

    private val menuProductMock = MenuProduct(
        uuid = productUuid,
        name = "name",
        newPrice = 1,
        oldPrice = 2,
        utils = "utils",
        nutrition = 3,
        description = "description",
        comboDescription = "comboDescription",
        photoLink = "photoLink",
        barcode = 0,
        isVisible = true,
        isRecommended = false,
        categoryUuids = listOf("1", "2")
    )

    private fun getMockCategory(uuid: String): Category {
        return Category(
            uuid = uuid,
            name = "Category $uuid",
            priority = 1
        )
    }
}
