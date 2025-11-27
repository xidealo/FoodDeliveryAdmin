package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.CreateCategory
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

class CreateCategoryUseCaseTest {
    private val categoryRepo: CategoryRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var createCategoryUseCase: CreateCategoryUseCase

    @BeforeTest
    fun setup() {
        createCategoryUseCase = CreateCategoryUseCase(categoryRepo, dataStoreRepo)
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null
            coEvery { dataStoreRepo.companyUuid } returns flowOf("companyUuid")

            assertFailsWith<NoTokenException> {
                createCategoryUseCase(categoryName = "New Category")
            }
        }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns "token"
            coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

            assertFailsWith<NoCompanyUuidException> {
                createCategoryUseCase(categoryName = "New Category")
            }
        }

    @Test
    fun `invoke throws CategoryNameException when category name is blank`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns "token"
            coEvery { dataStoreRepo.companyUuid } returns flowOf("companyUuid")

            assertFailsWith<CategoryNameException> {
                createCategoryUseCase(categoryName = "")
            }
        }

    @Test
    fun `invoke throws DuplicateCategoryNameException when name already exists`() =
        runTest {
            val token = "token"
            val companyUuid = "companyUuid"
            val existingCategory = Category(uuid = "1", name = "Duplicate", priority = 1)

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
            coEvery { categoryRepo.getCategoryList(token, companyUuid) } returns listOf(existingCategory)

            assertFailsWith<DuplicateCategoryNameException> {
                createCategoryUseCase(categoryName = "Duplicate")
            }
        }

    @Test
    fun `invoke posts new category with incremented priority`() =
        runTest {
            val token = "token"
            val companyUuid = "companyUuid"
            val existingCategories = listOf(Category(uuid = "1", name = "Existing", priority = 1))

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
            coEvery { categoryRepo.getCategoryList(token, companyUuid) } returns existingCategories

            createCategoryUseCase(categoryName = "New Category")

            coVerify {
                categoryRepo.postCategory(
                    token = token,
                    createCategory =
                        CreateCategory(
                            name = "New Category",
                            priority = existingCategories.size + 1,
                        ),
                )
            }
        }
}
