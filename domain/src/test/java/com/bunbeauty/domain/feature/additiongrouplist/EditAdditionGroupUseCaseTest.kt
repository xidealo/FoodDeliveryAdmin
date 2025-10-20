package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.AdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.DuplicateAdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.EditAdditionGroupUseCase
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.NotFindAdditionGroupException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EditAdditionGroupUseCaseTest {

    private val additionGroupRepo: AdditionGroupRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var editAdditionGroupUseCase: EditAdditionGroupUseCase

    @BeforeTest
    fun setup() {
        editAdditionGroupUseCase = EditAdditionGroupUseCase(
            additionGroupRepo = additionGroupRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke successfully updates addition group`() = runTest {
        // Given
        val token = "token"
        val uuid = "123"
        val existingGroup = AdditionGroup(
            uuid = uuid,
            name = "Old Name",
            isVisible = true,
            singleChoice = false,
            priority = 1
        )
        val updateAdditionGroup = updateAdditionGroupMock.copy(
            name = "Updated Name",
            isVisible = true,
            singleChoice = false,
            priority = 1
        )

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns listOf(existingGroup)
        coEvery { additionGroupRepo.updateAdditionGroup(updateAdditionGroup, token, uuid) } just Runs

        // When
        editAdditionGroupUseCase(uuid, updateAdditionGroup)

        // Then
        coVerify { additionGroupRepo.updateAdditionGroup(updateAdditionGroup, token, uuid) }
    }

    @Test
    fun `invoke throws NoTokenException when token is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns null

        assertFailsWith<NoTokenException> {
            editAdditionGroupUseCase("uuid", updateAdditionGroupMock.copy(name = "Name"))
        }
    }

    @Test
    fun `invoke throws NotFindAdditionGroupException when group not found`() = runTest {
        val token = "token"
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns emptyList()

        assertFailsWith<NotFindAdditionGroupException> {
            editAdditionGroupUseCase("uuid", updateAdditionGroupMock.copy(name = "Name"))
        }
    }

    @Test
    fun `invoke throws AdditionGroupNameException when name is blank`() = runTest {
        val token = "token"
        val uuid = "123"
        val existingGroup = AdditionGroup(uuid, "Old", 1, false, true)

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns listOf(existingGroup)

        assertFailsWith<AdditionGroupNameException> {
            editAdditionGroupUseCase(uuid, updateAdditionGroupMock.copy(name = ""))
        }
    }

    @Test
    fun `invoke throws DuplicateAdditionGroupNameException when name already exists`() = runTest {
        val token = "valid_token"
        val uuid = "123"
        val existingGroup1 = AdditionGroup(uuid, "Old Name", 1, false, true)
        val existingGroup2 = AdditionGroup("124", "Duplicate", 2, false, true)

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns listOf(existingGroup1, existingGroup2)

        assertFailsWith<DuplicateAdditionGroupNameException> {
            editAdditionGroupUseCase(uuid, updateAdditionGroupMock.copy(name = "Duplicate"))
        }
    }

    @Test
    fun `invoke does nothing when name and flags are unchanged`() = runTest {
        val token = "valid_token"
        val uuid = "123"
        val existingGroup = AdditionGroup(
            uuid = uuid,
            name = "SameName",
            isVisible = true,
            singleChoice = false,
            priority = 1
        )

        val update = UpdateAdditionGroup(
            name = "SameName",
            isVisible = true,
            singleChoice = false,
            priority = 1
        )

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { additionGroupRepo.getAdditionGroupList(token) } returns listOf(existingGroup)

        editAdditionGroupUseCase(uuid, update)

        coVerify(exactly = 0) {
            additionGroupRepo.updateAdditionGroup(any(), any(), any())
        }
    }

    private val updateAdditionGroupMock = UpdateAdditionGroup(
        name = "",
        isVisible = false,
        singleChoice = false,
        priority = 0
    )
}
