package com.bunbeauty.presentation

import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.use_case.GetMenuProductUseCase
import com.bunbeauty.domain.use_case.UpdateMenuProductUseCase
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductUIState
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.lang.Exception
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class EditMenuProductViewModelTest {
    private val getMenuProductUseCase: GetMenuProductUseCase = mockk()
    private val updateMenuProductUseCase: UpdateMenuProductUseCase = mockk()
    private lateinit var viewModel: EditMenuProductViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = EditMenuProductViewModel(
            getMenuProductUseCase = getMenuProductUseCase,
            updateMenuProductUseCase = updateMenuProductUseCase,
        )
    }

    @Test
    fun `return SUCCESS state when getMenuProductUseCase has no errors`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        // When
        viewModel.loadData(productUuidMock)
        // Then
        assertTrue(viewModel.state.value.editMenuProductState is EditMenuProductUIState.EditMenuProductState.Success)
        coVerify { getMenuProductUseCase(productUuidMock) }
    }

    @Test
    fun `return ERROR state when getMenuProductUseCase has errors`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } throws Exception()
        // When
        viewModel.loadData(productUuidMock)
        // Then
        assertTrue(viewModel.state.value.editMenuProductState is EditMenuProductUIState.EditMenuProductState.Error)
        coVerify { getMenuProductUseCase(productUuidMock) }
    }

    @Test
    fun `return new value name equals newName when onNameTextChanged was changed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        val name = "newName"
        // When
        viewModel.loadData(productUuidMock)
        viewModel.onNameTextChanged(name = name)
        val actualName =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).name
        // Then
        assertEquals(
            expected = name,
            actual = actualName
        )
    }

    @Test
    fun `return new value description equals newDescription when onDescriptionTextChanged was changed`() =
        runTest {
            // Given
            coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
            val description = "newDescription"
            // When
            viewModel.loadData(productUuidMock)
            viewModel.onDescriptionTextChanged(description = description)
            val actualDescription =
                (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).description
            // Then
            assertEquals(
                expected = description,
                actual = actualDescription
            )
        }

    @Test
    fun `return new value newPrice equals 2 when onNewPriceTextChanged was changed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        val newPrice = "2"
        // When
        viewModel.loadData(productUuidMock)
        viewModel.onNewPriceTextChanged(newPrice = newPrice)
        val actualNewPrice =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).newPrice
        // Then
        assertEquals(
            expected = newPrice,
            actual = actualNewPrice
        )
    }

    @Test
    fun `return new value nutrition equals 2 when onNutritionTextChanged was changed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        val nutrition = "2"
        // When
        viewModel.loadData(productUuidMock)
        viewModel.onNutritionTextChanged(nutrition = nutrition)
        val actualNutrition =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).nutrition
        // Then
        assertEquals(
            expected = nutrition,
            actual = actualNutrition
        )
    }

    @Test
    fun `return new value utils equals 2 when onSuggestedUtilsSelected was changed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        val suggestion = Suggestion(
            id = "2",
            value = "1"
        )
        // When
        viewModel.loadData(productUuidMock)
        viewModel.onSuggestedUtilsSelected(
            suggestion = suggestion
        )
        val actualSuggestion =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).utils
        // Then
        assertEquals(
            expected = suggestion.value,
            actual = actualSuggestion
        )
    }

    @Test
    fun `return new value oldPrice equals 2 when onOldPriceTextChanged was changed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        val oldPrice = "2"
        // When
        viewModel.loadData(productUuidMock)
        viewModel.onOldPriceTextChanged(oldPrice = oldPrice)
        val actualOldPrice =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).oldPrice
        // Then
        assertEquals(
            expected = oldPrice,
            actual = actualOldPrice
        )
    }

    @Test
    fun `return new value visible equals false when onVisibleChanged was changed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        val visible = false
        // When
        viewModel.loadData(productUuidMock)
        viewModel.onVisibleChanged(isVisible = false)
        val actualIsVisible =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).isVisible
        // Then
        assertEquals(
            expected = visible,
            actual = actualIsVisible
        )
    }

    private val productUuidMock: String = "2"

    private val menuProductMock = MenuProduct(
        uuid = productUuidMock,
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
        categories = emptyList(),
    )

}