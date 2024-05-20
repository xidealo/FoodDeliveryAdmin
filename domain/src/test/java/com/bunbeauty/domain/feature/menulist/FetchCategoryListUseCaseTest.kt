package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.menulist.FetchCategoryListUseCase
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class FetchCategoryListUseCaseTest {

    private val categoryRepo: CategoryRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var fetchCategoryListUseCase: FetchCategoryListUseCase

    @BeforeTest
    fun setup() {
        fetchCategoryListUseCase = FetchCategoryListUseCase(
            categoryRepo = categoryRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke calls fetchCategories with correct parameters`() = runTest {
        // Given
        val token = "test_token"
        val companyUuid = "test_company_uuid"
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { categoryRepo.fetchCategories(token = token, companyUuid = companyUuid) } returns emptyList()

        // When
        fetchCategoryListUseCase()

        // Then
        coVerify { categoryRepo.fetchCategories(token = token, companyUuid = companyUuid) }
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns null
        coEvery { dataStoreRepo.companyUuid } returns flowOf("test_company_uuid")

        // When & Then
        assertFailsWith<NoTokenException> {
            fetchCategoryListUseCase()
        }
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid is null`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns flow { }

        // When & Then
        assertFailsWith<NoCompanyUuidException> {
            fetchCategoryListUseCase()
        }
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid flow is empty`() = runTest {
        // Given
        coEvery { dataStoreRepo.getToken() } returns "test_token"
        coEvery { dataStoreRepo.companyUuid } returns flowOf()

        // When & Then
        assertFailsWith<NoCompanyUuidException> {
            fetchCategoryListUseCase()
        }
    }
}
