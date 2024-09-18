package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNutritionException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ValidateMenuProductNutritionUseCaseTest {

    private lateinit var validateMenuProductNutritionUseCase: ValidateMenuProductNutritionUseCase

    @BeforeTest
    fun setup() {
        validateMenuProductNutritionUseCase = ValidateMenuProductNutritionUseCase()
    }

    @Test
    fun `return null when nutrition is blank`() {
        val result = validateMenuProductNutritionUseCase(nutrition = "  ", units = "кг")

        assertNull(result)
    }

    @Test
    fun `throw MenuProductNutritionException when nutrition is not blank and units are empty`() {
        assertFailsWith<MenuProductNutritionException> {
            validateMenuProductNutritionUseCase(nutrition = "100", units = "")
        }
    }

    @Test
    fun `return nutrition when it and units are not blank`() {
        val expected = 100

        val result = validateMenuProductNutritionUseCase(nutrition = "100", units = "кг")

        assertEquals(expected, result)
    }
}
