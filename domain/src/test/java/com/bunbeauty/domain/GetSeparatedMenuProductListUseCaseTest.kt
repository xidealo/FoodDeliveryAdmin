package com.bunbeauty.domain

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.use_case.GetSeparatedMenuProductListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetSeparatedMenuProductListUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val menuProductRepo: MenuProductRepo = mockk()
    private lateinit var useCase: GetSeparatedMenuProductListUseCase
    @BeforeTest
    fun setup() {
        useCase = GetSeparatedMenuProductListUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
        )
    }

    @Test
    fun `return empty lists when menuProductRepo return empty list`() = runTest {
        // Given
        val companyUuid = "companyUuid"
        val isRefreshing = false
        coEvery { dataStoreRepo.companyUuid.firstOrNull() } returns companyUuid
        coEvery {
            menuProductRepo.getMenuProductList(
                companyUuid = companyUuid,
                isRefreshing = isRefreshing
            )
        } returns emptyList()

        // When
        val separatedMenuProductList = useCase(isRefreshing)
        // Then
        assertTrue(separatedMenuProductList.hiddenList.isEmpty() && separatedMenuProductList.visibleList.isEmpty())
    }

    @Test
    fun `return empty visible products when menuProductRepo return list with empty isVisible`() =
        runTest {
            // Given
            val token = "token"

            // When

            // Then
            // assertEquals(selectedCafe, result)
        }

    @Test
    fun `return empty hidden products when menuProductRepo return list with full isVisible`() =
        runTest {
            // Given
            val token = "token"

            // When

            // Then
            // assertEquals(selectedCafe, result)
        }

    @Test
    fun `return 2 hidden 3 visible products when menuProductRepo has same data by visible`() =
        runTest {
            // Given
            val token = "token"

            // When

            // Then
            // assertEquals(selectedCafe, result)
        }


    @Test
    fun `return sorted by name started with A and finished with Z when menuProductRepo has not empty list`() =
        runTest {
            // Given
            val token = "token"

            // When

            // Then
            // assertEquals(selectedCafe, result)
        }


}