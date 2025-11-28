package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EditCategoryUseCaseTest {

    private val categoryRepo: CategoryRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var editCategoryUseCase: EditCategoryUseCase

    @BeforeTest
    fun setup() {
        editCategoryUseCase = EditCategoryUseCase(categoryRepo, dataStoreRepo)
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns null
        coEvery { dataStoreRepo.companyUuid } returns flowOf("test_company_uuid")

        assertFailsWith<NoTokenException> {
            editCategoryUseCase(
                "category_uuid",
                UpdateCategory(name = "Updated Name", priority = 0)
            )
        }
    }

    @Test
    fun `invoke throws DuplicateCategoryNameException when name already exists`() = runTest {
        val token = "token"
        val companyUuid = "companyUuid"
        val existingCategory = Category(uuid = "1", name = "Duplicate", priority = 1)
        val editingCategory = Category(uuid = "2", name = "Old Name", priority = 1)

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            categoryRepo.getCategoryList(token = token, companyUuid = companyUuid)
        } returns listOf(existingCategory, editingCategory)

        assertFailsWith<DuplicateCategoryNameException> {
            editCategoryUseCase(
                "2",
                UpdateCategory(name = "Duplicate", priority = 0)
            )
        }
    }

    @Test
    fun `invoke throws CategoryNameException when category name is blank`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns flowOf("test_company_uuid")
        coEvery {
            categoryRepo.getCategoryList(any(), any())
        } returns listOf(Category(uuid = "1", name = "Old Name", priority = 1))

        assertFailsWith<CategoryNameException> {
            editCategoryUseCase(
                "1",
                UpdateCategory(name = "", priority = 0)
            )
        }
    }

    @Test
    fun `invoke does nothing when name is unchanged`() = runTest {
        val token = "test_token"
        val companyUuid = "test_company_uuid"
        val category = Category(uuid = "category_uuid", name = "Old Name", priority = 1)

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            categoryRepo.getCategoryList(token = token, companyUuid = companyUuid)
        } returns listOf(category)

        editCategoryUseCase(
            categoryUuid = category.uuid,
            updateCategory = UpdateCategory(name = "Old Name", priority = 1)
        )

        coVerify(exactly = 0) {
            categoryRepo.updateCategory(any(), any(), any())
        }
    }

    @Test
    fun `invoke successfully updates category`() = runTest {
        val token = "test_token"
        val companyUuid = "test_company_uuid"
        val updateCategory = UpdateCategory(name = "Updated Name", priority = 0)
        val categoryUuid = "category_uuid"
        val oldCategory = Category(uuid = categoryUuid, name = "Old Name", priority = 1)

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            categoryRepo.getCategoryList(token = token, companyUuid = companyUuid)
        } returns listOf(oldCategory)

        editCategoryUseCase(categoryUuid, updateCategory)

        coVerify(exactly = 1) {
            categoryRepo.updateCategory(
                categoryUuid = categoryUuid,
                updateCategory = updateCategory,
                token = token
            )
        }
    }
}
