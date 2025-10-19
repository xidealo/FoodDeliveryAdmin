package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct

import com.bunbeauty.domain.exception.NoAdditionGroupException
import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetAdditionGroupWithAdditionsForMenuUseCaseTest {

    private val menuProductRepo: MenuProductRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var getFilteredAdditionGroupWithAdditionsForMenuProductUseCase: GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase

    @BeforeTest
    fun setup() {
        getFilteredAdditionGroupWithAdditionsForMenuProductUseCase = GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `return addition group with additions when input is valid`() = runTest {
        // Given
        val menuProductUuid = "menu-uuid-1"
        val additionGroupUuid = "addition-group-1"
        val companyUuid = "company-uuid-1"

        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { menuProductRepo.getMenuProduct(companyUuid = companyUuid, menuProductUuid = menuProductUuid) } returns menuProductMock

        // When
        val result = getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(menuProductUuid, additionGroupUuid)

        // Then
        assertEquals(additionGroupWithAdditionsMock, result)
        coVerify { dataStoreRepo.companyUuid }
        coVerify { menuProductRepo.getMenuProduct(companyUuid = companyUuid, menuProductUuid = menuProductUuid) }
    }

    @Test
    fun `throw NoCompanyUuidException when companyUuid is null`() = runTest {
        // Given
        val menuProductUuid = "menu-uuid-1"
        val additionGroupUuid = "addition-group-1"
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

        // When & Then
        assertFailsWith<NoCompanyUuidException> {
            getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(menuProductUuid, additionGroupUuid)
        }
        coVerify { dataStoreRepo.companyUuid }
        coVerify(exactly = 0) { menuProductRepo.getMenuProduct(any(), any()) }
    }

    @Test
    fun `throw NoAdditionGroupException when addition group is not found`() = runTest {
        // Given
        val menuProductUuid = "menu-uuid-2"
        val additionGroupUuid = "non-existent-group"
        val companyUuid = "company-uuid-1"

        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { menuProductRepo.getMenuProduct(companyUuid = companyUuid, menuProductUuid = menuProductUuid) } returns menuProductMock

        // When & Then
        assertFailsWith<NoAdditionGroupException> {
            getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(menuProductUuid, additionGroupUuid)
        }
        coVerify { dataStoreRepo.companyUuid }
        coVerify { menuProductRepo.getMenuProduct(companyUuid = companyUuid, menuProductUuid = menuProductUuid) }
    }

    @Test
    fun `throw NoAdditionGroupException when menu product is null`() = runTest {
        // Given
        val menuProductUuid = "menu-uuid-1"
        val additionGroupUuid = "addition-group-1"
        val companyUuid = "company-uuid-1"

        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { menuProductRepo.getMenuProduct(companyUuid = companyUuid, menuProductUuid = menuProductUuid) } returns null

        // When & Then
        assertFailsWith<NoAdditionGroupException> {
            getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(menuProductUuid, additionGroupUuid)
        }
        coVerify { dataStoreRepo.companyUuid }
        coVerify { menuProductRepo.getMenuProduct(companyUuid = companyUuid, menuProductUuid = menuProductUuid) }
    }

    private val additionMock = Addition(
        uuid = "addition-1",
        name = "Addition 1",
        priority = 1,
        fullName = "Full Addition 1",
        price = 100,
        photoLink = "",
        isVisible = true,
        tag = null
    )

    private val additionGroupMock = AdditionGroup(
        uuid = "addition-group-1",
        name = "Group 1",
        priority = 1,
        isVisible = true,
        singleChoice = false
    )

    private val additionGroupWithAdditionsMock = AdditionGroupWithAdditions(
        additionGroup = additionGroupMock,
        additionList = listOf(additionMock)
    )

    private val menuProductMock = MenuProduct(
        uuid = "menu-uuid-1",
        name = "Menu Product 1",
        newPrice = 100,
        oldPrice = 120,
        units = "pcs",
        nutrition = 250,
        description = "",
        comboDescription = null,
        photoLink = "",
        barcode = 123456,
        isVisible = true,
        isRecommended = false,
        categoryUuids = emptyList(),
        additionGroups = listOf(additionGroupWithAdditionsMock)
    )
}
