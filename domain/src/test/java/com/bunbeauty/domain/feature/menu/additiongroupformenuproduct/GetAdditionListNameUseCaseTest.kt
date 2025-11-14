package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct

import com.bunbeauty.common.Constants
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.model.addition.Addition
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetAdditionListNameUseCaseTest {
    private lateinit var getAdditionListNameUseCase: GetAdditionListNameUseCase

    @BeforeTest
    fun setup() {
        getAdditionListNameUseCase = GetAdditionListNameUseCase()
    }

    @Test
    fun `return joined addition names when addition list is not empty`() {
        // Given
        val additions = listOf(sugarAddition, milkAddition)
        val expected = "Sugar ${Constants.BULLET_SYMBOL} Milk"

        // When
        val result = getAdditionListNameUseCase(additions)

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `return null when addition list is empty`() {
        // Given
        val additions = emptyList<Addition>()

        // When
        val result = getAdditionListNameUseCase(additions)

        // Then
        assertNull(result)
    }

    private val sugarAddition =
        Addition(
            uuid = "1",
            name = "Sugar",
            priority = 1,
            fullName = null,
            price = null,
            photoLink = "",
            isVisible = true,
            tag = null,
        )

    private val milkAddition =
        Addition(
            uuid = "2",
            name = "Milk",
            priority = 2,
            fullName = null,
            price = null,
            photoLink = "",
            isVisible = true,
            tag = null,
        )
}
