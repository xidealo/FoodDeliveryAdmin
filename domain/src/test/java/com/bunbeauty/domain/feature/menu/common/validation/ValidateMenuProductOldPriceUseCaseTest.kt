package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ValidateMenuProductOldPriceUseCaseTest {
    private lateinit var validateMenuProductOldPriceUseCase: ValidateMenuProductOldPriceUseCase

    @BeforeTest
    fun setup() {
        validateMenuProductOldPriceUseCase = ValidateMenuProductOldPriceUseCase()
    }

    @Test
    fun `return null when old price is blank`() {
        val result = validateMenuProductOldPriceUseCase(oldPrice = "  ", newPrice = 100)

        assertNull(result)
    }

    @Test
    fun `throw MenuProductOldPriceException when old price equals new price`() {
        assertFailsWith<MenuProductOldPriceException> {
            validateMenuProductOldPriceUseCase(oldPrice = "100", newPrice = 100)
        }
    }

    @Test
    fun `throw MenuProductOldPriceException when old price less new price`() {
        assertFailsWith<MenuProductOldPriceException> {
            validateMenuProductOldPriceUseCase(oldPrice = "100", newPrice = 110)
        }
    }

    @Test
    fun `return old price when it's greater than new price`() {
        val expected = 100

        val result = validateMenuProductOldPriceUseCase(oldPrice = "100", newPrice = 90)

        assertEquals(expected, result)
    }
}
