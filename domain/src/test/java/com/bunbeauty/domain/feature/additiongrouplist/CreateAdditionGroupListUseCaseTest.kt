package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.AdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.CreateAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.DuplicateAdditionGroupNameException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.CreateAdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CreateAdditionGroupListUseCaseTest {

    private val additionGroupRepo: AdditionGroupRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var createAdditionGroupListUseCase: CreateAdditionGroupListUseCase

    @BeforeTest
    fun setup() {
        createAdditionGroupListUseCase = CreateAdditionGroupListUseCase(
            additionGroupRepo = additionGroupRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke successfully posts addition group`() = runTest {
        val token = "test_token"
        val additionGroupName = "New Group"
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns emptyList()
        coEvery { additionGroupRepo.postAdditionGroup(token, any()) } returns createAdditionGroupMock

        createAdditionGroupListUseCase(
            additionName = additionGroupName,
            isVisible = true,
            singleChoice = false
        )

        coVerify {
            additionGroupRepo.postAdditionGroup(
                token,
                match { it.name == additionGroupName && it.isVisible && !it.singleChoice && it.priority == 0 }
            )
        }
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns null

        assertFailsWith<NoTokenException> {
            createAdditionGroupListUseCase(
                additionName = "Any",
                isVisible = true,
                singleChoice = false
            )
        }
    }

    @Test
    fun `invoke throws AdditionGroupNameException when name is blank`() = runTest {
        val token = "test_token"
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns emptyList()

        assertFailsWith<AdditionGroupNameException> {
            createAdditionGroupListUseCase(
                additionName = " ",
                isVisible = true,
                singleChoice = false
            )
        }
    }

    @Test
    fun `invoke throws DuplicateAdditionGroupNameException when name already exists`() = runTest {
        val token = "test_token"
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns listOf(
            AdditionGroup(
                uuid = "1",
                name = "Existing",
                priority = 0,
                isVisible = false,
                singleChoice = false
            )
        )

        assertFailsWith<DuplicateAdditionGroupNameException> {
            createAdditionGroupListUseCase(
                additionName = "Existing",
                isVisible = true,
                singleChoice = false
            )
        }
    }

    private val createAdditionGroupMock = CreateAdditionGroup(
        name = "",
        priority = 0,
        isVisible = false,
        singleChoice = false
    )
}
