package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GetCategoryUseCaseTest {

    private val categoryRepo: CategoryRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var getCategoryUseCase: GetCategoryUseCase

    @BeforeTest
    fun setup() {
        getCategoryUseCase = GetCategoryUseCase(categoryRepo, dataStoreRepo)
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns null
        coEvery { dataStoreRepo.companyUuid } returns flowOf("test_company_uuid")

        assertFailsWith<NoTokenException> {
            getCategoryUseCase("category_uuid")
        }
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

        assertFailsWith<NoCompanyUuidException> {
            getCategoryUseCase("category_uuid")
        }
    }

    @Test
    fun `invoke throws NotFoundCategoryException when category is not found`() = runTest {
        val token = "test_token"
        val companyUuid = "test_company_uuid"

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { categoryRepo.getCategory(companyUuid, "category_uuid", token) } returns null

        assertFailsWith<NotFoundCategoryException> {
            getCategoryUseCase("category_uuid")
        }
    }

    @Test
    fun `invoke returns category when found`() = runTest {
        val token = "test_token"
        val companyUuid = "test_company_uuid"
        val expectedCategory = Category.mock.copy(uuid = "category_uuid", name = "Test Category", priority = 1)

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { categoryRepo.getCategory(companyUuid, "category_uuid", token) } returns expectedCategory

        val result = getCategoryUseCase("category_uuid")

        assertEquals(expectedCategory, result)
    }
}
