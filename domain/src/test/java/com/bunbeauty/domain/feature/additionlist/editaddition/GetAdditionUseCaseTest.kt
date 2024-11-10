package com.bunbeauty.domain.feature.additionlist.editaddition

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additionlist.exception.NotFoundAdditionException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetAdditionUseCaseTest {
    private val additionRepo: AdditionRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var getAdditionUseCase: GetAdditionUseCase

    @BeforeTest
    fun setup() {
        getAdditionUseCase = GetAdditionUseCase(
            additionRepo = additionRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke should return Addition when addition is found`() = runTest {
        // Given
        val additionUuid = "uuid"
        val token = "valid_token"
        val addition = additionMock.copy(name = "Бекон", priority = 1, price = 1)

        coEvery { dataStoreRepo.token } returns flowOf(token)
        coEvery { additionRepo.getAddition(additionUuid, token) } returns addition

        // When
        val result = getAdditionUseCase(additionUuid)

        // Then
        assertEquals(addition, result)
        coVerify { additionRepo.getAddition(additionUuid, token) }
    }

    @Test
    fun `invoke should throw NoTokenException when token is null`() = runTest {
        // Given
        val additionUuid = "uuid"
        coEvery { dataStoreRepo.token } returns flowOf()

        // When / Then
        assertFailsWith<NoTokenException> {
            getAdditionUseCase(additionUuid)
        }
    }

    @Test
    fun `invoke should throw NotFoundAdditionException when addition is not found`() = runTest {
        // Given
        val additionUuid = "uuid"
        val token = "valid_token"
        coEvery { dataStoreRepo.token } returns flowOf(token)
        coEvery { additionRepo.getAddition(additionUuid, token) } returns null

        // When / Then
        assertFailsWith<NotFoundAdditionException> {
            getAdditionUseCase(additionUuid)
        }
    }

    private val additionMock = Addition(
        name = "",
        uuid = "uuid",
        priority = 0,
        fullName = "",
        price = 0,
        photoLink = "",
        isVisible = false
    )
}
