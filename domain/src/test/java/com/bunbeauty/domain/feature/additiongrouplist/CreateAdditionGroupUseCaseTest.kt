package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.AdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.CreateAdditionGroupUseCase
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.DuplicateAdditionGroupNameException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CreateAdditionGroupUseCaseTest {
    private val additionGroupRepo: AdditionGroupRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var createAdditionGroupUseCase: CreateAdditionGroupUseCase

    @BeforeTest
    fun setup() {
        createAdditionGroupUseCase =
            CreateAdditionGroupUseCase(
                additionGroupRepo = additionGroupRepo,
                dataStoreRepo = dataStoreRepo,
            )
    }

    @Test
    fun `invoke successfully posts addition group`() =
        runTest {
            val token = "test_token"
            val additionGroupName = "New Group"
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionGroupRepo.getAdditionGroupList(token) } returns emptyList()
            coEvery { additionGroupRepo.postAdditionGroup(token, any()) } returns additionGroupMock

            createAdditionGroupUseCase(
                additionName = additionGroupName,
                isVisible = true,
                singleChoice = false,
            )

            coVerify {
                additionGroupRepo.postAdditionGroup(
                    token,
                    match { it.name == additionGroupName && it.isVisible && !it.singleChoice && it.priority == 0 },
                )
            }
        }

    @Test
    fun `invoke throws NoTokenException when token is null`() =
        runTest {
            coEvery { dataStoreRepo.getToken() } returns null

            assertFailsWith<NoTokenException> {
                createAdditionGroupUseCase(
                    additionName = "Any",
                    isVisible = true,
                    singleChoice = false,
                )
            }
        }

    @Test
    fun `invoke throws AdditionGroupNameException when name is blank`() =
        runTest {
            val token = "test_token"
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionGroupRepo.getAdditionGroupList(token) } returns emptyList()

            assertFailsWith<AdditionGroupNameException> {
                createAdditionGroupUseCase(
                    additionName = " ",
                    isVisible = true,
                    singleChoice = false,
                )
            }
        }

    @Test
    fun `invoke throws DuplicateAdditionGroupNameException when name already exists`() =
        runTest {
            val token = "test_token"
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionGroupRepo.getAdditionGroupList(token) } returns
                listOf(
                    additionGroupMock,
                )

            assertFailsWith<DuplicateAdditionGroupNameException> {
                createAdditionGroupUseCase(
                    additionName = "Existing",
                    isVisible = true,
                    singleChoice = false,
                )
            }
        }

    private val additionGroupMock =
        AdditionGroup(
            uuid = "1",
            name = "Existing",
            priority = 0,
            isVisible = false,
            singleChoice = false,
        )
}
