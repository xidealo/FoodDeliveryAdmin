package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.domain.model.menuProcutToAdditionGroupToAddition.MenuProductToAdditionGroupToAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupToAdditionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GetSelectedAdditionListUseCaseTest {
    private val additionRepo: AdditionRepo = mockk()

    private val dataStoreRepo: DataStoreRepo = mockk()

    private val getFilteredAdditionGroupWithAdditionsForMenuProductUseCase: GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase =
        mockk()

    private val menuProductToAdditionGroupToAdditionRepository: MenuProductToAdditionGroupToAdditionRepository =
        mockk()
    private lateinit var getSelectedAdditionListUseCase: GetSelectedAdditionListUseCase

    @BeforeTest
    fun setup() {
        getSelectedAdditionListUseCase =
            GetSelectedAdditionListUseCase(
                additionRepo = additionRepo,
                dataStoreRepo = dataStoreRepo,
                menuProductToAdditionGroupToAdditionRepository = menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase = getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
    }

    @Test
    fun `return NoTokenException when empty token`() =
        runTest {
            val selectedGroupAdditionUuid = "uuid"
            val menuProductUuid = "uuid"
            // Given
            coEvery { dataStoreRepo.getToken() } returns null
            // When & Then
            assertFailsWith<NoTokenException> {
                getSelectedAdditionListUseCase(
                    selectedGroupAdditionUuid = selectedGroupAdditionUuid,
                    menuProductUuid = menuProductUuid,
                    editedAdditionListUuid = emptyList(),
                )
            }
            coVerify { dataStoreRepo.getToken() }
            confirmVerified(
                dataStoreRepo,
                additionRepo,
                menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
        }

    @Test
    fun `return additionUuid when updateEditAdditionList is not empty`() =
        runTest {
            val selectedGroupAdditionUuid = "AdditionUuid"
            val menuProductUuid = "productUuid"

            coEvery { dataStoreRepo.getToken() } returns null
            assertFailsWith<NoTokenException> {
                getSelectedAdditionListUseCase(
                    selectedGroupAdditionUuid = selectedGroupAdditionUuid,
                    menuProductUuid = menuProductUuid,
                    editedAdditionListUuid = emptyList(),
                )
            }
            coVerify { dataStoreRepo.getToken() }
            confirmVerified(
                dataStoreRepo,
                additionRepo,
                menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
        }

    @Test
    fun `return additions based on edited list when editedAdditionListUuid is not empty`() =
        runTest {
            // Given
            val token = "token"
            val productUuid = "productUuid"
            val groupUuid = "groupUuid"
            val commonAdditionList =
                listOf(
                    Addition.mock.copy(uuid = "uuid1", name = "Addition 1"),
                    Addition.mock.copy(uuid = "uuid2", name = "Addition 2"),
                    Addition.mock.copy(uuid = "uuid3", name = "Addition 3"),
                )

            val additionGroupWithAdditions =
                AdditionGroupWithAdditions.mock.copy(additionList = commonAdditionList)

            val editedAdditionListUuid =
                listOf(
                    "uuid1",
                    "uuid3",
                )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionRepo.getAdditionList(token) } returns commonAdditionList
            coEvery {
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = productUuid,
                    additionGroupForMenuUuid = groupUuid,
                )
            } returns additionGroupWithAdditions

            // When
            val result =
                getSelectedAdditionListUseCase(
                    selectedGroupAdditionUuid = groupUuid,
                    menuProductUuid = productUuid,
                    editedAdditionListUuid = editedAdditionListUuid,
                )

            // Then
            assertEquals(2, result.selectedAdditionList.size)
            assertEquals(1, result.notSelectedAdditionList.size)
            assertEquals(listOf("uuid1", "uuid3"), result.selectedAdditionList.map { it.uuid })
            assertEquals(listOf("uuid2"), result.notSelectedAdditionList.map { it.uuid })

            coVerify {
                dataStoreRepo.getToken()
                additionRepo.getAdditionList(token)
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = productUuid,
                    additionGroupForMenuUuid = groupUuid,
                )
            }
            confirmVerified(
                dataStoreRepo,
                additionRepo,
                menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
        }

    @Test
    fun `return empty selected list when selectedGroupAdditionUuid is null`() =
        runTest {
            // Given
            val token = "token"
            val productUuid = "productUuid"

            val commonAdditions =
                listOf(
                    Addition.mock.copy(uuid = "add1", name = "Addition 1"),
                    Addition.mock.copy(uuid = "add2", name = "Addition 2"),
                )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionRepo.getAdditionList(token) } returns commonAdditions

            // When
            val result =
                getSelectedAdditionListUseCase(
                    selectedGroupAdditionUuid = null,
                    menuProductUuid = productUuid,
                    editedAdditionListUuid = emptyList(),
                )

            // Then
            assertEquals(emptyList<Addition>(), result.selectedAdditionList)
            assertEquals(commonAdditions, result.notSelectedAdditionList)

            coVerify {
                dataStoreRepo.getToken()
                additionRepo.getAdditionList(token)
            }
            confirmVerified(
                dataStoreRepo,
                additionRepo,
                menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
        }

    @Test
    fun `return empty selected list when uuidList is empty`() =
        runTest {
            // Given
            val token = "token"
            val productUuid = "productUuid"
            val groupUuid = "groupUuid"
            val commonAdditions =
                listOf(
                    Addition.mock.copy(uuid = "add1", name = "Addition 1"),
                    Addition.mock.copy(uuid = "add2", name = "Addition 2"),
                )

            val additionGroupWithAdditions =
                AdditionGroupWithAdditions.mock.copy(additionList = emptyList())

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionRepo.getAdditionList(token) } returns commonAdditions
            coEvery {
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = productUuid,
                    additionGroupForMenuUuid = groupUuid,
                )
            } returns additionGroupWithAdditions

            // When
            val result =
                getSelectedAdditionListUseCase(
                    selectedGroupAdditionUuid = groupUuid,
                    menuProductUuid = productUuid,
                    editedAdditionListUuid = emptyList(),
                )

            // Then
            assertEquals(emptyList<Addition>(), result.selectedAdditionList)
            assertEquals(commonAdditions, result.notSelectedAdditionList)

            coVerify {
                dataStoreRepo.getToken()
                additionRepo.getAdditionList(token)
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = productUuid,
                    additionGroupForMenuUuid = groupUuid,
                )
            }
            confirmVerified(
                dataStoreRepo,
                additionRepo,
                menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
        }

    @Test
    fun `return separated additions when menu product has additions`() =
        runTest {
            // Given
            val token = "token"
            val commonAdditions =
                listOf(
                    Addition.mock.copy(uuid = "add1", name = "Addition 1"),
                    Addition.mock.copy(uuid = "add2", name = "Addition 2"),
                    Addition.mock.copy(uuid = "add3", name = "Addition 3"),
                    Addition.mock.copy(uuid = "add4", name = "Addition 4"),
                )

            val menuProductAdditions =
                listOf(
                    MenuProductToAdditionGroupToAddition.mock.copy(additionUuid = "add1"),
                    MenuProductToAdditionGroupToAddition.mock.copy(additionUuid = "add3"),
                )

            val filteredAdditions =
                listOf(
                    Addition.mock.copy(uuid = "add1", name = "Addition 1"),
                    Addition.mock.copy(uuid = "add3", name = "Addition 3"),
                )
            val uuidList = listOf("add1", "add3")

            val additionGroupWithAdditions =
                AdditionGroupWithAdditions.mock.copy(additionList = filteredAdditions)

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionRepo.getAdditionList(token) } returns commonAdditions
            coEvery {
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = "product1",
                    additionGroupForMenuUuid = "group1",
                )
            } returns additionGroupWithAdditions
            coEvery {
                menuProductToAdditionGroupToAdditionRepository.getMenuProductToAdditionGroupToAdditionList(uuidList)
            } returns menuProductAdditions

            // When
            val result =
                getSelectedAdditionListUseCase(
                    selectedGroupAdditionUuid = "group1",
                    menuProductUuid = "product1",
                    editedAdditionListUuid = emptyList(),
                )

            // Then
            assertEquals(2, result.selectedAdditionList.size)
            assertEquals(2, result.notSelectedAdditionList.size)
            assertEquals(listOf("add1", "add3"), result.selectedAdditionList.map { it.uuid })
            assertEquals(listOf("add2", "add4"), result.notSelectedAdditionList.map { it.uuid })

            coVerify {
                dataStoreRepo.getToken()
                additionRepo.getAdditionList(token)
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = "product1",
                    additionGroupForMenuUuid = "group1",
                )
                menuProductToAdditionGroupToAdditionRepository.getMenuProductToAdditionGroupToAdditionList(uuidList)
            }
            confirmVerified(
                dataStoreRepo,
                additionRepo,
                menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
        }

    @Test
    fun `return empty lists when common addition list is empty`() =
        runTest {
            // Given
            val token = "token"

            val additionGroupWithAdditions =
                AdditionGroupWithAdditions.mock.copy(additionList = emptyList())

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionRepo.getAdditionList(token) } returns emptyList()
            coEvery {
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = "product1",
                    additionGroupForMenuUuid = "group1",
                )
            } returns additionGroupWithAdditions

            // When
            val result =
                getSelectedAdditionListUseCase(
                    selectedGroupAdditionUuid = "group1",
                    menuProductUuid = "product1",
                    editedAdditionListUuid = emptyList(),
                )

            // Then
            assertEquals(emptyList<Addition>(), result.selectedAdditionList)
            assertEquals(emptyList<Addition>(), result.notSelectedAdditionList)

            coVerify {
                dataStoreRepo.getToken()
                additionRepo.getAdditionList(token)
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                    menuProductUuid = "product1",
                    additionGroupForMenuUuid = "group1",
                )
            }
            confirmVerified(
                dataStoreRepo,
                additionRepo,
                menuProductToAdditionGroupToAdditionRepository,
                getFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
            )
        }
}
