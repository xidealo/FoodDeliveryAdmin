package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValidateMenuProductCategoriesUseCaseTest {

    private lateinit var validateMenuProductCategoriesUseCase: ValidateMenuProductCategoriesUseCase

    @BeforeTest
    fun setup() {
        validateMenuProductCategoriesUseCase = ValidateMenuProductCategoriesUseCase()
    }

    @Test
    fun `throw MenuProductCategoriesException when no categories`() {
        assertFailsWith<MenuProductCategoriesException> {
            validateMenuProductCategoriesUseCase(categories = emptyList())
        }
    }

    @Test
    fun `return categories when they are not empty`() {
        val categories = listOf(
            SelectableCategory(
                category = Category(
                    uuid = "uuid",
                    name = "category",
                    priority = 1,
                ),
                selected = true
            )
        )

        val result = validateMenuProductCategoriesUseCase(categories = categories)

        assertEquals(categories, result)
    }

}