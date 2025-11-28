package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SaveCategoryListUseCaseTest {
    private val categoryRepo: CategoryRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var saveCategoryListUseCase: SaveCategoryListUseCase

    private val categoryList =
        listOf(
            Category(uuid = "1", name = "Category 1", priority = 1),
            Category(uuid = "2", name = "Category 2", priority = 2),
        )

    @BeforeTest
    fun setup() {
        saveCategoryListUseCase = SaveCategoryListUseCase(categoryRepo, dataStoreRepo)
    }

    @Test
    fun `invoke saves category list when token is available`() =
        runTest {
            // Given
            val token = "valid_token"
            coEvery { dataStoreRepo.getToken() } returns token

            // When
            saveCategoryListUseCase(categoryList)

            // Then
            coVerify {
                categoryRepo.saveCategoryPriority(
                    token = token,
                    category = categoryList,
                )
            }
        }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            // Given
            coEvery { dataStoreRepo.getToken() } returns null

            // Then
            assertFailsWith<NoTokenException> {
                saveCategoryListUseCase(categoryList)
            }
        }
}
