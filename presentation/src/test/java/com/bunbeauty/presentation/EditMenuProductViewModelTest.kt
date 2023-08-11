package com.bunbeauty.presentation

import com.bunbeauty.domain.use_case.GetMenuProductUseCase
import com.bunbeauty.domain.use_case.UpdateMenuProductUseCase
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditMenuProductViewModelTest {
    private val getMenuProductUseCase: GetMenuProductUseCase = mockk()
    private val updateMenuProductUseCase: UpdateMenuProductUseCase = mockk()
    private lateinit var viewModel: EditMenuProductViewModel

    @BeforeTest
    fun setup() {
        viewModel = EditMenuProductViewModel(
            getMenuProductUseCase = getMenuProductUseCase,
            updateMenuProductUseCase = updateMenuProductUseCase,
        )
    }

    @Test
    fun `return SUCCESS state when getMenuProductUseCase has no errors`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return ERROR state when getMenuProductUseCase has errors`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return new value todoWrite when onNameTextChanged was changed`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return new value todoWrite when onDescriptionTextChanged was changed`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return new value todoWrite when onNewPriceTextChanged was changed`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return new value todoWrite when onNutritionTextChanged was changed`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return new value todoWrite when onSuggestedUtilsSelected was changed`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return new value todoWrite when onOldPriceTextChanged was changed`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `return new value todoWrite when onVisibleChanged was changed`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }
}