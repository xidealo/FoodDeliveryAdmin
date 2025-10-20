package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GetAdditionGroupListFromMenuProductUseCaseTest {
    private val menuProductRepo: MenuProductRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val getFilteredAdditionGroupWithAdditionsForMenuProductUseCase: GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase =
        mockk()
    private lateinit var getAdditionGroupListFromMenuProductUseCase: GetAdditionGroupListFromMenuProductUseCase

    @BeforeTest
    fun setup() {
        getAdditionGroupListFromMenuProductUseCase = GetAdditionGroupListFromMenuProductUseCase(
            menuProductRepo = menuProductRepo,
            dataStoreRepo = dataStoreRepo,
            getFilteredAdditionGroupWithAdditionsForMenuProductUseCase = getFilteredAdditionGroupWithAdditionsForMenuProductUseCase
        )
    }

    @Test
    fun `invoke throws NoCompanyUuidException when companyUuid is null`() = runTest {
        // Given
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()

        // When & Then
        assertFailsWith<NoCompanyUuidException> {
            getAdditionGroupListFromMenuProductUseCase("uuid")
        }
    }

    @Test
    fun `invoke returns addition groups when menu product exists`() = runTest {
        // Given
        val companyUuid = "123"
        val menuUuid = "uuid"
        val additionGroup = additionGroupMock
        val additionGroupWithAdditions = AdditionGroupWithAdditions(
            additionGroup = additionGroup,
            additionList = listOf(additionMock)
        )
        val menuProductWithGroups = menuProductMock.copy(
            additionGroups = listOf(additionGroupWithAdditions)
        )

        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            menuProductRepo.getMenuProduct(
                menuUuid,
                companyUuid
            )
        } returns menuProductWithGroups

        // When
        val result = getAdditionGroupListFromMenuProductUseCase(menuUuid)

        // Then
        assertEquals(1, result.size)
        assertEquals("Name", result.first().additionGroup.name)
        coVerify { menuProductRepo.getMenuProduct(menuUuid, companyUuid) }
    }

    @Test
    fun `invoke returns empty list when menu product is not found`() = runTest {
        // Given
        val companyUuid = "123"
        val menuUuid = "uuid"

        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery { menuProductRepo.getMenuProduct(menuUuid, companyUuid) } returns null

        // When
        val result = getAdditionGroupListFromMenuProductUseCase(menuUuid)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke returns empty list when menu product has no addition groups`() = runTest {
        // Given
        val companyUuid = "123"
        val menuUuid = "uuid"

        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            menuProductRepo.getMenuProduct(
                menuUuid,
                companyUuid
            )
        } returns menuProductMock.copy(
            additionGroups = emptyList()
        )

        // When
        val result = getAdditionGroupListFromMenuProductUseCase(menuUuid)

        // Then
        assertTrue(result.isEmpty())
    }

    private val menuProductMock = MenuProduct(
        uuid = "",
        name = "",
        newPrice = 500,
        oldPrice = null,
        units = null,
        nutrition = null,
        description = "",
        comboDescription = null,
        photoLink = "",
        barcode = null,
        isVisible = true,
        isRecommended = false,
        categoryUuids = emptyList(),
        additionGroups = emptyList()
    )

    private val additionMock = Addition(
        uuid = "",
        name = "",
        priority = 1,
        fullName = "",
        price = 1,
        photoLink = "",
        isVisible = true,
        tag = ""
    )

    private val additionGroupMock = AdditionGroup(
        uuid = "123",
        name = "Name",
        priority = 1,
        singleChoice = false,
        isVisible = true
    )
}
