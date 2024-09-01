package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.GetCategoryListUseCase
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
        getCategoryListUseCase = GetCategoryListUseCase(
            categoryRepo = categoryRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke returns filtered category list`() = runTest {
        // Given
        val token = "test_token"
        val companyUuid = "test_company_uuid"
        val categories = listOf(
            categoryMock.copy(uuid = "1"),
            categoryMock.copy(uuid = "2"),
            categoryMock.copy(uuid = "")
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { categoryRepo.getCategoryList(token, companyUuid) } returns categories

        // When
        val result = getCategoryListUseCase()

        // Then
        assertEquals(listOf(categoryMock.copy(uuid = "1"), categoryMock.copy(uuid = "2")), result)
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns null
        coEvery { dataStoreRepo.companyUuid } returns flowOf("test_company_uuid")

        // When & Then
        assertFailsWith<NoTokenException> {
            getCategoryListUseCase()
        }
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid is null`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

        // When & Then
        assertFailsWith<NoCompanyUuidException> {
            getCategoryListUseCase()
        }
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid flow is empty`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns flowOf()

        // When & Then
        assertFailsWith<NoCompanyUuidException> {
            getCategoryListUseCase()
        }
    }

    private val categoryMock = Category(
        uuid = "",
        name = "",
        priority = 0
    )
}
