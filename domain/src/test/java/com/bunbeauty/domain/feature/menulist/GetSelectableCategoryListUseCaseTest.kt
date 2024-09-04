package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.feature.menu.common.GetCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.GetSelectableCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSelectableCategoryListUseCaseTest {

    private val getCategoryListUseCase: GetCategoryListUseCase = mockk {
        coEvery { this@mockk.invoke() } returns listOf(
            getMockCategory(1),
            getMockCategory(2),
            getMockCategory(3),
        )
    }
    private lateinit var getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase

    @BeforeTest
    fun setup() {
        getSelectableCategoryListUseCase = GetSelectableCategoryListUseCase(
            getCategoryListUseCase = getCategoryListUseCase
        )
    }

    @Test
    fun `returns all not selected categories when input uuid list is empty`() = runTest {
        val expected = listOf(
            SelectableCategory(
                category = getMockCategory(1),
                selected = false
            ),
            SelectableCategory(
                category = getMockCategory(2),
                selected = false
            ),
            SelectableCategory(
                category = getMockCategory(3),
                selected = false
            ),
        )

        val result = getSelectableCategoryListUseCase(selectedCategoryUuidList = emptyList())

        assertEquals(expected, result)
    }

    @Test
    fun `returns selected categories when input uuid list is not empty`() = runTest {
        val expected = listOf(
            SelectableCategory(
                category = getMockCategory(1),
                selected = true
            ),
            SelectableCategory(
                category = getMockCategory(2),
                selected = false
            ),
            SelectableCategory(
                category = getMockCategory(3),
                selected = true
            ),
        )

        val result = getSelectableCategoryListUseCase(
            selectedCategoryUuidList = listOf("1", "3")
        )

        assertEquals(expected, result)
    }

    private fun getMockCategory(priority: Int): Category {
        return Category(
            "$priority",
            "Category $priority",
            priority,
        )
    }

}