package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UpdateVisibleAdditionUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val additionRepo: AdditionRepo = mockk()
    private lateinit var useCase: UpdateVisibleAdditionUseCase

    @BeforeTest
    fun setup() {
        useCase = UpdateVisibleAdditionUseCase(
            additionRepo = additionRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `should throw NoTokenException when token is null`() = runTest {
        coEvery { dataStoreRepo.getToken() } returns null

        assertFailsWith(
            exceptionClass = NoTokenException::class,
            block = { useCase(additionUuid = "", isVisible = true) }
        )
    }

    @Test
    fun `should call updateAddition function when token non null`() = runTest {
        // Given
        val token = "token"
        val additionUuidMock = ""
        val isVisible = true
        val updateAdditionMock = UpdateAddition(isVisible = isVisible)
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            additionRepo.updateAddition(
                updateAddition = updateAdditionMock,
                token = token,
                additionUuid = additionUuidMock
            )
        } returns Unit

        // When
        useCase(additionUuid = additionUuidMock, isVisible = isVisible)

        // Then
        coVerify {
            additionRepo.updateAddition(
                updateAddition = updateAdditionMock,
                token = token,
                additionUuid = additionUuidMock
            )
        }
    }
}
