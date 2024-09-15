package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValidateMenuProductNewPriceUseCaseTest {

    private lateinit var validateMenuProductNewPriceUseCase: ValidateMenuProductNewPriceUseCase

    @BeforeTest
    fun setup() {
        validateMenuProductNewPriceUseCase = ValidateMenuProductNewPriceUseCase()
    }

    @Test
    fun `throw MenuProductNewPriceException when new price is blank`() {
        assertFailsWith<MenuProductNewPriceException> {
            validateMenuProductNewPriceUseCase(newPrice = "  ")
        }
    }

    @Test
    fun `throw MenuProductNewPriceException when new price equals zero`() {
        assertFailsWith<MenuProductNewPriceException> {
            validateMenuProductNewPriceUseCase(newPrice = "0")
        }
    }

    @Test
    fun `return new price when it's greater than zero`() {
        val expected = 100

        val result = validateMenuProductNewPriceUseCase(newPrice = "100")

        assertEquals(expected, result)
    }
}