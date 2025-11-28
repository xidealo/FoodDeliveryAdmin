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

class GetCategoryListUseCaseTest {

    private val categoryRepo: CategoryRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var getCategoryListUseCase: GetCategoryListUseCase

    @BeforeTest
    fun setup() {
        getCategoryListUseCase = GetCategoryListUseCase(categoryRepo, dataStoreRepo)
    }

    @Test
    fun `invoke filters out categories with empty UUID`() = runTest {
        // Given
        mockSuccessfulDataFetch(
            token = "test_token",
            companyUuid = "test_company_uuid",
            categories = listOf(
                categoryMock.copy(uuid = "1", priority = 5),
                categoryMock.copy(uuid = "2", priority = 2),
                categoryMock.copy(uuid = "")
            )
        )

        // When
        val result = getCategoryListUseCase(refreshing = false)

        // Then

        assertEquals(
            listOf(
                categoryMock.copy(uuid = "2", priority = 2),
                categoryMock.copy(uuid = "1", priority = 5)
            ),
            result
        )
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns null
        coEvery { dataStoreRepo.companyUuid } returns flowOf("test_company_uuid")

        assertFailsWith<NoTokenException> { getCategoryListUseCase(refreshing = false) }
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

        assertFailsWith<NoCompanyUuidException> { getCategoryListUseCase(refreshing = false) }
    }

    private fun mockSuccessfulDataFetch(
        token: String,
        companyUuid: String,
        categories: List<Category>
    ) {
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { categoryRepo.getCategoryList(token, companyUuid, any()) } returns categories
    }

    private val categoryMock = Category(uuid = "", name = "", priority = 0)
}
