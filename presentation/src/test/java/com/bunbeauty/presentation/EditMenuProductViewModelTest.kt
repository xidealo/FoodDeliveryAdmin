package com.bunbeauty.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.use_case.GetMenuProductUseCase
import com.bunbeauty.domain.use_case.UpdateMenuProductUseCase
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductEvent
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
import kotlin.test.assertFalse
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
        with(viewModel) {
            loadData(productUuidMock)
            onNameTextChanged(name = name)
        }

        // Then
        val actualName =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).name
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
            with(viewModel) {
                loadData(productUuidMock)
                onDescriptionTextChanged(description = description)
            }

            // Then
            val actualDescription =
                (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).description
            assertEquals(
                expected = description,
                actual = actualDescription
            )
        }

    @Test
    fun `return new value comboDescription equals newDescription when onDescriptionTextChanged was changed`() =
        runTest {
            // Given
            coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
            val comboDescription = "comboDescription"

            // When
            with(viewModel) {
                loadData(productUuidMock)
                onComboDescriptionTextChanged(comboDescription = comboDescription)
            }

            // Then
            val actualDescription =
                (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).comboDescription
            assertEquals(
                expected = comboDescription,
                actual = actualDescription
            )
        }

    @Test
    fun `return new value newPrice equals 2 when onNewPriceTextChanged was changed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        val newPrice = "2"
        // When

        with(viewModel) {
            loadData(productUuidMock)
            onNewPriceTextChanged(newPrice = newPrice)
        }

        // Then
        val actualNewPrice =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).newPrice
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
        with(viewModel) {
            loadData(productUuidMock)
            onNutritionTextChanged(nutrition = nutrition)
        }

        // Then
        val actualNutrition =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).nutrition
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
        with(viewModel) {
            loadData(productUuidMock)
            onSuggestedUtilsSelected(suggestion = suggestion)
        }

        // Then
        val actualSuggestion =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).utils
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
        with(viewModel) {
            loadData(productUuidMock)
            onOldPriceTextChanged(oldPrice = oldPrice)
        }

        // Then
        val actualOldPrice =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).oldPrice
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
        with(viewModel) {
            loadData(productUuidMock)
            onVisibleChanged(isVisible = false)
        }

        // Then
        val actualIsVisible =
            (viewModel.state.value.editMenuProductState as EditMenuProductUIState.EditMenuProductState.Success).isVisible
        assertEquals(
            expected = visible,
            actual = actualIsVisible
        )
    }

    @Test
    fun `return ShowUpdateProductSuccess event when update is success`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        coEvery { updateMenuProductUseCase(any()) } returns Unit

        // When
        with(viewModel) {
            loadData(productUuidMock)
            updateMenuProduct()
        }

        // Then
        coVerify { updateMenuProductUseCase(any()) }
        assertTrue(
            viewModel.state.value.editMenuProductState is EditMenuProductUIState.EditMenuProductState.Success
        )
        assertEquals(
            expected = EditMenuProductEvent.ShowUpdateProductSuccess(
                menuProductMock.name
            ),
            actual = viewModel.state.value.eventList.first()
        )
    }

    @Test
    fun `return hasNameError true when update failed by name product`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        coEvery { updateMenuProductUseCase(any()) } throws MenuProductNameException()

        // When
        with(viewModel) {
            loadData(productUuidMock)
            updateMenuProduct()
        }

        // Then
        assertTrue(
            actual = (viewModel.state.value.editMenuProductState
                    as EditMenuProductUIState.EditMenuProductState.Success).hasNameError
        )
    }

    @Test
    fun `return hasNewPriceError true when update failed by new price product`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        coEvery { updateMenuProductUseCase(any()) } throws MenuProductNewPriceException()

        // When
        with(viewModel) {
            loadData(productUuidMock)
            updateMenuProduct()
        }

        // Then
        assertTrue(
            actual = (viewModel.state.value.editMenuProductState
                    as EditMenuProductUIState.EditMenuProductState.Success).hasNewPriceError
        )
    }

    @Test
    fun `return hasDescriptionError true when update failed by description product`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        coEvery { updateMenuProductUseCase(any()) } throws MenuProductDescriptionException()

        // When
        with(viewModel) {
            loadData(productUuidMock)
            updateMenuProduct()
        }

        // Then
        assertTrue(
            actual = (viewModel.state.value.editMenuProductState
                    as EditMenuProductUIState.EditMenuProductState.Success).hasDescriptionError
        )
    }

    @Test
    fun `return loadingButton false when update failed`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        coEvery { updateMenuProductUseCase(any()) } throws Exception()

        // When
        with(viewModel) {
            loadData(productUuidMock)
            updateMenuProduct()
        }

        // Then
        assertFalse(
            actual = (viewModel.state.value.editMenuProductState
                    as EditMenuProductUIState.EditMenuProductState.Success).isLoadingButton
        )
    }

    @Test
    fun `return ShowUpdateProductError event when update failed by unknown exception`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        coEvery { updateMenuProductUseCase(any()) } throws Exception()

        // When
        with(viewModel) {
            loadData(productUuidMock)
            updateMenuProduct()
        }

        // Then
        coVerify { updateMenuProductUseCase(any()) }
        assertTrue(
            viewModel.state.value.editMenuProductState is EditMenuProductUIState.EditMenuProductState.Success
        )
        assertEquals(
            expected = EditMenuProductEvent.ShowUpdateProductError(
                menuProductMock.name
            ),
            actual = viewModel.state.value.eventList.first()
        )
    }

    @Test
    fun `return 0 events when consumeEvents was called`() = runTest {
        // Given
        coEvery { getMenuProductUseCase(productUuidMock) } returns menuProductMock
        coEvery { updateMenuProductUseCase(any()) } throws Exception()

        // When
        with(viewModel) {
            loadData(productUuidMock)
            updateMenuProduct()
            updateMenuProduct()
            updateMenuProduct()
            updateMenuProduct()
            consumeEvents(
                listOf(
                    EditMenuProductEvent.ShowUpdateProductError(
                        menuProductMock.name
                    )
                )
            )
        }
        // Then
        assertTrue(
            actual = viewModel.state.value.eventList.isEmpty()
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