package com.bunbeauty.domain.feature.menu.common.validation

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNutritionException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValidateMenuProductNutritionUseCaseTest {

    private lateinit var validateMenuProductNutritionUseCase: ValidateMenuProductNutritionUseCase

    @BeforeTest
    fun setup() {
        validateMenuProductNutritionUseCase = ValidateMenuProductNutritionUseCase()
    }

    @Test
    fun `return zero when nutrition is blank`() {
        val expected = 0

        val result = validateMenuProductNutritionUseCase(nutrition = "  ", units = "кг")

        assertEquals(expected, result)
    }

    @Test
    fun `throw MenuProductNutritionException when nutrition is not blank and units are blank`() {
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