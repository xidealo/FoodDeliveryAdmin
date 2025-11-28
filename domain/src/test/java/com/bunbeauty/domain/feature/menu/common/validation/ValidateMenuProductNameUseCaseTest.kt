package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValidateMenuProductNameUseCaseTest {
    private lateinit var validateMenuProductNameUseCase: ValidateMenuProductNameUseCase

    @BeforeTest
    fun setup() {
        validateMenuProductNameUseCase = ValidateMenuProductNameUseCase()
    }

    @Test
    fun `throw MenuProductNameException when name is blank`() {
        assertFailsWith<MenuProductNameException> {
            validateMenuProductNameUseCase(name = "  ")
        }
    }

    @Test
    fun `return name when it's not blank`() {
        val name = "name"

        val result = validateMenuProductNameUseCase(name = name)

        assertEquals(name, result)
    }
}
