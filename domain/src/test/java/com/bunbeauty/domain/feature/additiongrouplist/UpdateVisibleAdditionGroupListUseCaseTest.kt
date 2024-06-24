package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
@OptIn(ExperimentalCoroutinesApi::class)
class UpdateVisibleAdditionGroupListUseCaseTest {
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val additionGroupRepo: AdditionGroupRepo = mockk()
    private lateinit var useCase: UpdateVisibleAdditionGroupListUseCase

    @BeforeTest
    fun setup() {
        useCase = UpdateVisibleAdditionGroupListUseCase(
            additionGroupRepo = additionGroupRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `should throw NoTokenException when token is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns null

        assertFailsWith(
            exceptionClass = NoTokenException::class,
            block = { useCase(additionUuidGroup = "", isVisible = true) }
        )
    }

    @Test
    fun `should call updateAdditionGroup function when token non null`() = runTest {
        // Given
        val token = "token"
        val additionGroupUuidMock = ""
        val isVisible = true
        val updateAdditionGroupMock = UpdateAdditionGroup(isVisible = isVisible)
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            additionGroupRepo.updateAdditionGroup(
                updateAdditionGroup = updateAdditionGroupMock,
                token = token,
                additionGroupUuid = additionGroupUuidMock
            )
        } returns Unit

        // When
        useCase(additionUuidGroup = additionGroupUuidMock, isVisible = isVisible)

        // Then
        coVerify {
            additionGroupRepo.updateAdditionGroup(
                updateAdditionGroup = updateAdditionGroupMock,
                token = token,
                additionGroupUuid = additionGroupUuidMock
            )
        }
    }
}
