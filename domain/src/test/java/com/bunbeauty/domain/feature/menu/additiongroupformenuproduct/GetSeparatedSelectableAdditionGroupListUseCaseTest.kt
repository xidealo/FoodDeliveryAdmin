package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.SeparatedAdditionGroupList
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.GetSeparatedSelectableAdditionGroupListUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.SeparatedSelectableAdditionList
import com.bunbeauty.domain.feature.menu.editmenuproduct.GetMenuProductUseCase
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.domain.model.menuprocuttoadditiongroup.MenuProductToAdditionGroup
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class GetSeparatedSelectableAdditionGroupListUseCaseTest {
    private lateinit var useCase: GetSeparatedSelectableAdditionGroupListUseCase
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase = mockk()
    private val menuProductToAdditionGroupRepository: MenuProductToAdditionGroupRepository = mockk()
    private val getMenuProductUseCase: GetMenuProductUseCase = mockk()

    @Before
    fun setUp() {
        useCase = GetSeparatedSelectableAdditionGroupListUseCase(
            getSeparatedAdditionGroupListUseCase,
            menuProductToAdditionGroupRepository,
            getMenuProductUseCase
        )

        // Common mock setup
        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(any()) } returns null
        coEvery { getMenuProductUseCase(any()) } returns MenuProduct.mock.copy(
            "",
            "",
            additionGroups = emptyList()
        )
    }

    @Test
    fun `invoke should return empty lists when separatedAdditionGroupList is empty`() = runTest {
        // Arrange
        coEvery { getSeparatedAdditionGroupListUseCase(any()) } returns
                SeparatedAdditionGroupList(emptyList(), emptyList())

        // Act
        val result = useCase(false, null, "menu-product-uuid", null)

        // Assert
        assertEquals(SeparatedSelectableAdditionList(emptyList(), emptyList()), result)
    }

    @Test
    fun `invoke should filter out contained addition groups from visible list`() = runTest {
        // Arrange
        val separatedAdditionGroupList = SeparatedAdditionGroupList(
            visibleList = listOf(
                AdditionGroup.mock.copy("group1", "Group 1"),
                AdditionGroup.mock.copy("group2", "Group 2")
            ),
            hiddenList = emptyList()
        )

        coEvery { getSeparatedAdditionGroupListUseCase(any()) } returns separatedAdditionGroupList
        coEvery {
            menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(any())
        } returns MenuProductToAdditionGroup.mock.copy(
            "main-uuid",
            "menu-product-uuid",
            "group1"
        )

        coEvery { getMenuProductUseCase(any()) } returns MenuProduct.mock.copy(
            "",
            "",
            additionGroups = listOf(
                AdditionGroupWithAdditions.mock.copy(
                    AdditionGroup.mock.copy("group1", "Group 1"),
                    emptyList()
                )
            )
        )

        // Act
        val result = useCase(false, null, "menu-product-uuid", null)

        // Assert
        assertEquals(1, result.visibleList.size)
    }

    @Test
    fun `invoke should not filter main edited addition group even if contained`() = runTest {
        // Arrange
        val separatedAdditionGroupList = SeparatedAdditionGroupList(
            visibleList = listOf(AdditionGroup.mock.copy("group1", "Group 1")),
            hiddenList = emptyList()
        )

        coEvery { getSeparatedAdditionGroupListUseCase(any()) } returns separatedAdditionGroupList
        coEvery { getMenuProductUseCase(any()) } returns MenuProduct.mock.copy(
            "", "",
            additionGroups = listOf(
                AdditionGroupWithAdditions.mock.copy(
                    AdditionGroup.mock.copy(
                        "group1",
                        "Group 1"
                    ),
                    emptyList()
                )
            )
        )
        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("main-uuid") } returns
                MenuProductToAdditionGroup.mock.copy(
                    uuid = "main-uuid",
                    menuProductUuid = "group1",
                    additionGroupUuid = "menu-product-uuid"
                )

        // Act
        val result = useCase(false, null, "menu-product-uuid", "main-uuid")

        // Assert
        assertEquals(1, result.visibleList.size)
    }

    @Test
    fun `invoke should set isSelected true for selected addition group`() = runTest {
        // Arrange
        val separatedAdditionGroupList = SeparatedAdditionGroupList(
            visibleList = listOf(AdditionGroup.mock.copy("group1", "Group 1")),
            hiddenList = emptyList()
        )

        coEvery { getSeparatedAdditionGroupListUseCase(any()) } returns separatedAdditionGroupList

        // Act
        val result = useCase(false, "group1", "menu-product-uuid", null)

        // Assert
        assertEquals(true, result.visibleList[0].isSelected)
    }

    @Test
    fun `invoke should process both visible and hidden lists`() = runTest {
        // Arrange
        val separatedAdditionGroupList = SeparatedAdditionGroupList(
            visibleList = listOf(AdditionGroup.mock.copy("group1", "Group 1")),
            hiddenList = listOf(AdditionGroup.mock.copy("group2", "Group 2"))
        )

        coEvery { getSeparatedAdditionGroupListUseCase(any()) } returns separatedAdditionGroupList

        // Act
        val result = useCase(false, null, "menu-product-uuid", null)

        // Assert
        assertEquals(2, result.visibleList.size + result.hiddenList.size)
    }

}