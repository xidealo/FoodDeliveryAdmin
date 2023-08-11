package com.bunbeauty.domain

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.use_case.UpdateMenuProductUseCase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateMenuProductUseCaseTest {

    private val menuProductRepo: MenuProductRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var useCase: UpdateMenuProductUseCase

    @BeforeTest
    fun setup() {
        useCase = UpdateMenuProductUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
        )
    }

    @Test
    fun `throw NoTokenException when token is null`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `throw MenuProductNameException when name is empty`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `throw MenuProductNewPriceException when newPrice is empty`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `throw MenuProductDescriptionException when description is empty`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }

    @Test
    fun `verify calling updateMenuProduct from menuProductRepo when data is ok`() = runTest {
        // Given

        // When

        // Then
        //assertEquals(selectedCafe, result)
    }
}