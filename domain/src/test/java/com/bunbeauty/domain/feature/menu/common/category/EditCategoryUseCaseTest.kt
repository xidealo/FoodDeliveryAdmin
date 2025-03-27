package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
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
            editCategoryUseCase("category_uuid", UpdateCategory(name = "Updated Name", priority = 0))
        }
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

        assertFailsWith<NoCompanyUuidException> {
            editCategoryUseCase("category_uuid", UpdateCategory(name = "Updated Name", priority = 0))
        }
    }

    @Test
    fun `invoke throws CategoryNameException when category name is blank`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns flowOf("test_company_uuid")

        assertFailsWith<CategoryNameException> {
            editCategoryUseCase("category_uuid", UpdateCategory(name = "", priority = 0))
        }
    }

    @Test
    fun `invoke successfully updates category`() = runTest {
        val token = "test_token"
        val companyUuid = "test_company_uuid"
        val updateCategory = UpdateCategory(name = "Updated Name", priority = 0)
        val categoryUuid = "category_uuid"

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)

        editCategoryUseCase(categoryUuid, updateCategory)

        coVerify {
            categoryRepo.updateCategory(
                categoryUuid = categoryUuid,
                companyUuid = companyUuid,
                updateCategory = updateCategory,
                token = token
            )
        }
    }
}
