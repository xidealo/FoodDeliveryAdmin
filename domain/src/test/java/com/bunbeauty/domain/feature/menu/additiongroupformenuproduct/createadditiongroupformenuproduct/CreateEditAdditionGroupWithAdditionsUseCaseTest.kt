package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.createadditiongroupformenuproduct

import com.bunbeauty.domain.exception.NoAdditionGroupException
import com.bunbeauty.domain.exception.NoAdditionListException
import com.bunbeauty.domain.repo.MenuProductRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CreateEditAdditionGroupWithAdditionsUseCaseTest {
    private val menuProductRepo: MenuProductRepo = mockk()

    private lateinit var createEditAdditionGroupWithAdditionsUseCase: CreateEditAdditionGroupWithAdditionsUseCase

    @BeforeTest
    fun setUp() {
        createEditAdditionGroupWithAdditionsUseCase =
            CreateEditAdditionGroupWithAdditionsUseCase(
                menuProductRepo = menuProductRepo,
            )
    }

    @Test
    fun `return NoAdditionGroupException when additionGroupUuid null`() =
        runTest {
            // Given
            val additionList =
                listOf(
                    "Addition1",
                    "Addition2",
                )
            val menuProductUuid = "menuUuid"

            assertFailsWith<NoAdditionGroupException> {
                createEditAdditionGroupWithAdditionsUseCase(
                    menuProductUuid = menuProductUuid,
                    additionGroupUuid = null,
                    additionList = additionList,
                )
            }

            coVerify(exactly = 0) {
                menuProductRepo.createMenuProductAdditions(any(), any(), any())
            }
            confirmVerified(menuProductRepo)
        }

    @Test
    fun `return NoAdditionListException when additionList isEmpty`() =
        runTest {
            val menuProductUuid = "menuUuid"
            val additionGroupUuid = "additionGroupUuid"

            assertFailsWith<NoAdditionListException> {
                createEditAdditionGroupWithAdditionsUseCase(
                    menuProductUuid = menuProductUuid,
                    additionGroupUuid = additionGroupUuid,
                    additionList = emptyList(),
                )
            }

            coVerify(exactly = 0) {
                menuProductRepo.createMenuProductAdditions(any(), any(), any())
            }
            confirmVerified(menuProductRepo)
        }

    @Test
    fun `return createMenuProductAdditions when success`() =
        runTest {
            val additionList =
                listOf(
                    "Addition1",
                    "Addition2",
                )
            val menuProductUuid = "menuUuid"
            val additionGroupUuid = "additionGroupUuid"

            coEvery {
                menuProductRepo.createMenuProductAdditions(
                    menuProductUuid = menuProductUuid,
                    additionGroupUuid = additionGroupUuid,
                    additionList = additionList,
                )
            } returns Unit

            createEditAdditionGroupWithAdditionsUseCase(
                menuProductUuid = menuProductUuid,
                additionGroupUuid = additionGroupUuid,
                additionList = additionList,
            )

            coVerify(exactly = 1) {
                menuProductRepo.createMenuProductAdditions(
                    menuProductUuid = menuProductUuid,
                    additionGroupUuid = additionGroupUuid,
                    additionList = additionList,
                )
            }
            confirmVerified(menuProductRepo)
        }
}
