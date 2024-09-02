package com.bunbeauty.domain.feature.additionlist.editaddition

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriceException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.usecase.UpdateAdditionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UpdateAdditionUseCaseTest {
    private val additionRepo: AdditionRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var updateAdditionUseCase: UpdateAdditionUseCase

    @BeforeTest
    fun setup() {
        updateAdditionUseCase = UpdateAdditionUseCase(
            additionRepo = additionRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke successfully update addition`() = runTest {
        // Given
        val token = "valid_token"
        val additionUuidMock = "uuid"
        val updateAddition = updateAdditionMock.copy(
            name = "Бекон",
            priority = 1,
            price = 10
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            additionRepo.updateAddition(
                updateAddition,
                token,
                additionUuidMock
            )
        } returns Unit

        // When
        updateAdditionUseCase.invoke(additionUuidMock, updateAddition)

        // Then
        coVerify { additionRepo.updateAddition(updateAddition, token, additionUuidMock) }
        coVerify { dataStoreRepo.getToken() }
    }

    @Test
    fun `invoke should throw NoTokenException when token is null`() = runTest {
        // Given
        val additionUuidMock = "uuid"
        val updateAddition = updateAdditionMock.copy(
            name = "Бекон",
            priority = 1,
            price = 10
        )
        coEvery { dataStoreRepo.getToken() } returns null

        // When & Then
        assertFailsWith<NoTokenException> {
            updateAdditionUseCase.invoke(additionUuidMock, updateAddition)
        }
    }

    @Test
    fun `invoke should throw AdditionNameException when name isNullOrBlank`() = runTest {
        // Given
        val token = "valid_token"
        val additionUuidMock = "uuid"
        val updateAddition = updateAdditionMock.copy(
            name = "",
            priority = 1,
            price = 10
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<AdditionNameException> {
            updateAdditionUseCase.invoke(additionUuidMock, updateAddition)
        }
    }

    @Test
    fun `invoke should throw AdditionPriorityException when priority is null`() = runTest {
        // Given
        val token = "valid_token"
        val additionUuidMock = "uuid"
        val updateAddition = updateAdditionMock.copy(
            name = "Бекон",
            priority = null,
            price = 10
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<AdditionPriorityException> {
            updateAdditionUseCase.invoke(additionUuidMock, updateAddition)
        }
    }

    @Test
    fun `invoke should throw AdditionPriceException when price is zero`() = runTest {
        // Given
        val token = "valid_token"
        val additionUuidMock = "uuid"
        val updateAddition = updateAdditionMock.copy(
            name = "Бекон",
            priority = 1,
            price = 0
        )
        coEvery { dataStoreRepo.getToken() } returns token

        // When & Then
        assertFailsWith<AdditionPriceException> {
            updateAdditionUseCase.invoke(additionUuidMock, updateAddition)
        }
    }

    private val updateAdditionMock = UpdateAddition(
        name = "",
        priority = 0,
        fullName = "",
        price = 0,
        photoLink = "",
        isVisible = false
    )
}
