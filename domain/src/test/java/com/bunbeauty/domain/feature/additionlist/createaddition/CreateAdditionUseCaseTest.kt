package com.bunbeauty.domain.feature.additionlist.createaddition

import com.bunbeauty.domain.feature.additionlist.exception.AdditionNotCreatedException
import com.bunbeauty.domain.feature.additionlist.validation.ValidateAdditionNameUseCase
import com.bunbeauty.domain.feature.additionlist.validation.ValidateAdditionNewPriceUseCase
import com.bunbeauty.domain.feature.additionlist.validation.ValidateAdditionPriorityUseCase
import com.bunbeauty.domain.model.addition.CreateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateAdditionUseCaseTest {

    private val name = "name"
    private val fullName = "fullName"
    private val price = "100"
    private val priceInt = 100
    private val priority = "1"
    private val tokenMock = "token"
    private val photoLink = "photoLink"

    private val validateAdditionNameUseCase: ValidateAdditionNameUseCase = mockk {
        every { this@mockk.invoke(name) } returns name
    }

    private val validateAdditionNewPriceUseCase: ValidateAdditionNewPriceUseCase = mockk {
        every { this@mockk.invoke(price) } returns priceInt
    }

    private val validateAdditionPriorityUseCase: ValidateAdditionPriorityUseCase = mockk {
        every { this@mockk.invoke(priority) } returns priority
    }

    private val additionRepo: AdditionRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk {
        coEvery { getToken() } returns tokenMock
    }
    private lateinit var createAdditionUseCase: CreateAdditionUseCase

    @BeforeTest
    fun setup() {
        createAdditionUseCase = CreateAdditionUseCase(
            validateCreateAdditionNewPriceUseCase = validateAdditionNewPriceUseCase,
            validateCreateAdditionNameUseCase = validateAdditionNameUseCase,
            validateCreateAdditionPriorityUseCase = validateAdditionPriorityUseCase,
            additionRepo = additionRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `throw AdditionNotCreatedException when saving failed`() = runTest {
        coEvery {
            additionRepo.post(
                token = tokenMock,
                createAddition = CreateAddition(
                    name = name,
                    price = priceInt,
                    priority = priority,
                    fullName = fullName,
                    isVisible = true,
                    photoLink = photoLink
                )
            )
        } returns null

        assertFailsWith<AdditionNotCreatedException> {
            createAdditionUseCase(
                CreateAdditionUseCase.Params(
                    name = name,
                    price = price,
                    priority = priority,
                    photoLink = photoLink,
                    fullName = fullName,
                    isVisible = true

                )
            )
        }
    }

    @Test
    fun `return Unit when successfully saved`() = runTest {
        coEvery {
            additionRepo.post(
                token = tokenMock,
                createAddition = CreateAddition(
                    name = name,
                    price = priceInt,
                    priority = priority,
                    fullName = fullName,
                    isVisible = true,
                    photoLink = photoLink
                )
            )
        } returns mockk()

        val result = createAdditionUseCase(
            CreateAdditionUseCase.Params(
                name = name,
                price = price,
                priority = priority,
                photoLink = photoLink,
                fullName = fullName,
                isVisible = true
            )
        )
        assertEquals(Unit, result)
    }
}
