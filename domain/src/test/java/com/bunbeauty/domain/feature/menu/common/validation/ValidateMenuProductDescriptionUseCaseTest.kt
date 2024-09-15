package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValidateMenuProductDescriptionUseCaseTest {

    private lateinit var validateMenuProductDescriptionUseCase: ValidateMenuProductDescriptionUseCase

    @BeforeTest
    fun setup() {
        validateMenuProductDescriptionUseCase = ValidateMenuProductDescriptionUseCase()
    }

    @Test
    fun `throw MenuProductDescriptionException when description is blank`() {
        assertFailsWith<MenuProductDescriptionException> {
            validateMenuProductDescriptionUseCase(description = "  ")
        }
    }

    @Test
    fun `return description when it's not blank`() {
        val description = "description"

        val result = validateMenuProductDescriptionUseCase(description = description)

        assertEquals(description, result)
    }

}