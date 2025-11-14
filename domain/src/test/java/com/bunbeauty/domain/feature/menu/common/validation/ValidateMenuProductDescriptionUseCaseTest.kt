package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionLongException
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

    @Test
    fun `throw MenuProductDescriptionLongException when description is longer than 512 characters`() {
        val longDescription = "x".repeat(513)

        assertFailsWith<MenuProductDescriptionLongException> {
            validateMenuProductDescriptionUseCase(longDescription)
        }
    }

    @Test
    fun `accept exactly 512 characters`() {
        val description = "x".repeat(512)

        val result = validateMenuProductDescriptionUseCase(description = description)

        assertEquals(description, result)
    }
}
