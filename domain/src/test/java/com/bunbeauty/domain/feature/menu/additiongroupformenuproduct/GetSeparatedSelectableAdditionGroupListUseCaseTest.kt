package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.GetSeparatedSelectableAdditionGroupListUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.GetMenuProductUseCase
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository
import io.mockk.mockk
import org.junit.Before

class GetSeparatedSelectableAdditionGroupListUseCaseTest {
    private lateinit var useCase: GetSeparatedSelectableAdditionGroupListUseCase
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase = mockk()
    private val menuProductToAdditionGroupRepository: MenuProductToAdditionGroupRepository = mockk()
    private val getMenuProductUseCase: GetMenuProductUseCase = mockk()

    @Before
    fun setUp() {
        useCase =
            GetSeparatedSelectableAdditionGroupListUseCase(
                getSeparatedAdditionGroupListUseCase,
                menuProductToAdditionGroupRepository,
                getMenuProductUseCase,
            )
    }

//    @Test
//    fun `invoke should return filtered and mapped selectable addition groups`() = runTest {
//        // Arrange
//        val refreshing = false
//        val selectedAdditionGroupUuid = "selected-uuid"
//        val menuProductUuid = "menu-product-uuid"
//        val mainEditedAdditionGroupUuid = "main-edited-uuid"
//
//        val separatedAdditionGroupList = SeparatedAdditionGroupList.mock.copy(
//            visibleList = listOf(
//                AdditionGroup.mock.copy(uuid = "group1", name = "Group 1"),
//                AdditionGroup.mock.copy(uuid = "group2", name = "Group 2"),
//                AdditionGroup.mock.copy(uuid = "group3", name = "Group 3")
//            ),
//            hiddenList = listOf(
//                AdditionGroup.mock.copy(uuid = "group4", name = "Group 4"),
//                AdditionGroup.mock.copy(uuid = "group5", name = "Group 5")
//            )
//        )
//
//        coEvery { getSeparatedAdditionGroupListUseCase(refreshing) } returns separatedAdditionGroupList
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("selected-uuid") } returns
//                MenuProductToAdditionGroup.mock.copy("selected-uuid", "group2", "menu-product-uuid")
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("main-edited-uuid") } returns
//                MenuProductToAdditionGroup.mock.copy(
//                    "main-edited-uuid",
//                    "group1",
//                    "menu-product-uuid"
//                )
//
//        val menuProduct = MenuProduct.mock.copy(
//            uuid = menuProductUuid,
//            name = "Test Product",
//            additionGroups = listOf(
//                AdditionGroupWithAdditions.mock.copy(
//                    additionGroup = AdditionGroup.mock.copy(uuid = "group1", name = "Group 1"),
//                    additionList = emptyList()
//                ),
//                AdditionGroupWithAdditions.mock.copy(
//                    additionGroup = AdditionGroup.mock.copy(uuid = "group3", name = "Group 3"),
//                    additionList = emptyList()
//                )
//            )
//        )
//        coEvery { getMenuProductUseCase(menuProductUuid) } returns menuProduct
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("group1") } returns
//                MenuProductToAdditionGroup("uuid1", "group1", "menu-product-uuid")
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("group3") } returns
//                MenuProductToAdditionGroup("uuid3", "group3", "menu-product-uuid")
//
//        // Act
//        val result = useCase(
//            refreshing,
//            selectedAdditionGroupUuid,
//            menuProductUuid,
//            mainEditedAdditionGroupUuid
//        )
//
//        // Assert
//        assertEquals(2, result.visibleList.size) // group2 (not contained) and group1 (main edited)
//        assertEquals(1, result.hiddenList.size) // group5 (group4 is contained and not main edited)
//
//        assertEquals("group2", result.visibleList[0].uuid)
//        assertEquals(true, result.visibleList[0].isSelected) // selected group
//
//        assertEquals("group1", result.visibleList[1].uuid)
//        assertEquals(false, result.visibleList[1].isSelected)
//
//        assertEquals("group5", result.hiddenList[0].uuid)
//        assertEquals(false, result.hiddenList[0].isSelected)
//    }
//
//    @Test
//    fun `invoke should handle null selectedAdditionGroupUuid`() = runTest {
//        // Arrange
//        val separatedAdditionGroupList = SeparatedAdditionGroupList(
//            visibleList = listOf(AdditionGroup("group1", "Group 1")),
//            hiddenList = emptyList()
//        )
//
//        coEvery { getSeparatedAdditionGroupListUseCase(any()) } returns separatedAdditionGroupList
//        coEvery { getMenuProductUseCase(any()) } returns MenuProduct("", "", emptyList())
//
//        // Act
//        val result = useCase(false, null, "menu-product-uuid", null)
//
//        // Assert
//        assertEquals(1, result.visibleList.size)
//        assertEquals(false, result.visibleList[0].isSelected)
//    }
//
//    @Test
//    fun `invoke should handle null mainEditedAdditionGroupUuid`() = runTest {
//        // Arrange
//        val separatedAdditionGroupList = SeparatedAdditionGroupList(
//            visibleList = listOf(AdditionGroup("group1", "Group 1")),
//            hiddenList = emptyList()
//        )
//
//        coEvery { getSeparatedAdditionGroupListUseCase(any()) } returns separatedAdditionGroupList
//        coEvery { getMenuProductUseCase(any()) } returns MenuProduct("", "", emptyList())
//
//        // Act
//        val result = useCase(false, "selected-uuid", "menu-product-uuid", null)
//
//        // Assert
//        assertEquals(1, result.visibleList.size)
//    }
//
//    @Test
//    fun `getSelectedAdditionGroupUuid should return additionGroupUuid when found`() = runTest {
//        // Arrange
//        val uuid = "test-uuid"
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(uuid) } returns
//                MenuProductToAdditionGroup(uuid, "addition-group-uuid", "menu-product-uuid")
//
//        // Act
//        val result = useCase.getSelectedAdditionGroupUuid(uuid)
//
//        // Assert
//        assertEquals("addition-group-uuid", result)
//    }
//
//    @Test
//    fun `getSelectedAdditionGroupUuid should return original uuid when not found`() = runTest {
//        // Arrange
//        val uuid = "test-uuid"
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(uuid) } returns null
//
//        // Act
//        val result = useCase.getSelectedAdditionGroupUuid(uuid)
//
//        // Assert
//        assertEquals(uuid, result)
//    }
//
//    @Test
//    fun `getSelectedAdditionGroupUuid should return null for null input`() = runTest {
//        // Act
//        val result = useCase.getSelectedAdditionGroupUuid(null)
//
//        // Assert
//        assertEquals(null, result)
//    }
//
//    @Test
//    fun `getMainAdditionGroupUuid should return additionGroupUuid when found`() = runTest {
//        // Arrange
//        val uuid = "test-uuid"
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(uuid) } returns
//                MenuProductToAdditionGroup(uuid, "addition-group-uuid", "menu-product-uuid")
//
//        // Act
//        val result = useCase.getMainAdditionGroupUuid(uuid)
//
//        // Assert
//        assertEquals("addition-group-uuid", result)
//    }
//
//    @Test
//    fun `getMainAdditionGroupUuid should return null when not found`() = runTest {
//        // Arrange
//        val uuid = "test-uuid"
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(uuid) } returns null
//
//        // Act
//        val result = useCase.getMainAdditionGroupUuid(uuid)
//
//        // Assert
//        assertEquals(null, result)
//    }
//
//    @Test
//    fun `getMainAdditionGroupUuid should return null for null input`() = runTest {
//        // Act
//        val result = useCase.getMainAdditionGroupUuid(null)
//
//        // Assert
//        assertEquals(null, result)
//    }
//
//    @Test
//    fun `getContainedAdditionGroupUuidList should return list of addition group uuids`() = runTest {
//        // Arrange
//        val menuProductUuid = "menu-product-uuid"
//        val menuProduct = MenuProduct(
//            menuProductUuid,
//            "Test Product",
//            listOf(
//                AdditionGroupWithAdditions(AdditionGroup("group1", "Group 1"), emptyList()),
//                AdditionGroupWithAdditions(AdditionGroup("group2", "Group 2"), emptyList())
//            )
//        )
//
//        coEvery { getMenuProductUseCase(menuProductUuid) } returns menuProduct
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("group1") } returns
//                MenuProductToAdditionGroup("uuid1", "group1", "menu-product-uuid")
//        coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("group2") } returns
//                MenuProductToAdditionGroup("uuid2", "group2", "menu-product-uuid")
//
//        // Act
//        val result = useCase.getContainedAdditionGroupUuidList(menuProductUuid)
//
//        // Assert
//        assertEquals(listOf("group1", "group2"), result)
//    }
//
//    @Test
//    fun `getContainedAdditionGroupUuidList should handle null menu product to addition group`() =
//        runTest {
//            // Arrange
//            val menuProductUuid = "menu-product-uuid"
//            val menuProduct = MenuProduct(
//                menuProductUuid,
//                "Test Product",
//                listOf(
//                    AdditionGroupWithAdditions(AdditionGroup("group1", "Group 1"), emptyList()),
//                    AdditionGroupWithAdditions(AdditionGroup("group2", "Group 2"), emptyList())
//                )
//            )
//
//            coEvery { getMenuProductUseCase(menuProductUuid) } returns menuProduct
//            coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("group1") } returns null
//            coEvery { menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup("group2") } returns
//                    MenuProductToAdditionGroup("uuid2", "group2", "menu-product-uuid")
//
//            // Act
//            val result = useCase.getContainedAdditionGroupUuidList(menuProductUuid)
//
//            // Assert
//            assertEquals(listOf("group2"), result)
//        }
//
//    @Test
//    fun `toSelectableAdditionGroupItem should create correct selectable item`() {
//        // Arrange
//        val additionGroup = AdditionGroup("group1", "Group 1")
//        val selectedAdditionGroupUuid = "group1"
//
//        // Act
//        val result = useCase.toSelectableAdditionGroupItem(additionGroup, selectedAdditionGroupUuid)
//
//        // Assert
//        assertEquals("group1", result.uuid)
//        assertEquals("Group 1", result.name)
//        assertEquals(true, result.isSelected)
//    }
//
//    @Test
//    fun `toSelectableAdditionGroupItem should set isSelected to false when uuids dont match`() {
//        // Arrange
//        val additionGroup = AdditionGroup("group1", "Group 1")
//        val selectedAdditionGroupUuid = "group2"
//
//        // Act
//        val result = useCase.toSelectableAdditionGroupItem(additionGroup, selectedAdditionGroupUuid)
//
//        // Assert
//        assertEquals(false, result.isSelected)
//    }
}
