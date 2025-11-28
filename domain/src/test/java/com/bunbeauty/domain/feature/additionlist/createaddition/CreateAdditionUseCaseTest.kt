package com.bunbeauty.domain.feature.additionlist.createaddition

import com.bunbeauty.common.Constants.ADDITION_HEIGHT
import com.bunbeauty.common.Constants.ADDITION_WIDTH
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPhotoException
import com.bunbeauty.domain.feature.additionlist.CreateAdditionUseCase
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.addition.CreateAdditionModel
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import kotlin.test.Test

class CreateAdditionUseCaseTest {

    private val additionRepo: AdditionRepo = mockk()
    private val uploadPhotoUseCase: UploadPhotoUseCase = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private var useCase: CreateAdditionUseCase =
        CreateAdditionUseCase(additionRepo, uploadPhotoUseCase, dataStoreRepo)

    @Test
    fun `invoke should create addition successfully`() = runTest {
        // Arrange
        val params = mockParams.copy(
            name = "Test Addition",
            isVisible = true,
            newImageUri = "content://image.jpg",
            price = 100,
            fullName = "Full Test Addition",
            priority = 1,
            tag = "test_tag"
        )

        val token = "test_token"
        val photoUrl = "https://example.com/photo.jpg"

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            uploadPhotoUseCase(
                "content://image.jpg",
                ADDITION_WIDTH,
                ADDITION_HEIGHT
            )
        } returns Photo(photoUrl)
        coEvery { additionRepo.createAddition(token, any()) } returns Unit

        // Act
        useCase(params)

        // Assert
        coVerify {
            additionRepo.createAddition(
                token = token,
                createAdditionModel = CreateAdditionModel(
                    name = "Test Addition",
                    isVisible = true,
                    price = 100,
                    photoLink = photoUrl,
                    fullName = "Full Test Addition",
                    priority = 1,
                    tag = "test_tag"
                )
            )
        }
    }

    @Test
    fun `invoke should throw NoTokenException when token is null`() = runTest {
        // Arrange
        val params = mockParams.copy(
            name = "Test Addition",
            isVisible = true,
            newImageUri = "content://image.jpg"
        )

        coEvery { dataStoreRepo.getToken() } returns null

        // Act & Assert
        assertThrows(NoTokenException::class.java) {
            runBlocking { useCase(params) }
        }
    }

    @Test
    fun `invoke should throw AdditionNameException when name is blank`() = runTest {
        // Arrange
        val params = mockParams.copy(
            name = " ",
            isVisible = true,
            newImageUri = "content://image.jpg"
        )

        coEvery { dataStoreRepo.getToken() } returns "token"

        // Act & Assert
        assertThrows(AdditionNameException::class.java) {
            runBlocking { useCase(params) }
        }
    }

    @Test
    fun `invoke should throw AdditionPhotoException when imageUri is null`() = runTest {
        // Arrange
        val params = mockParams.copy(
            name = "Test Addition",
            isVisible = true,
            newImageUri = null
        )

        coEvery { dataStoreRepo.getToken() } returns "token"

        // Act & Assert
        assertThrows(AdditionPhotoException::class.java) {
            runBlocking { useCase(params) }
        }
    }

    @Test
    fun `invoke should throw AdditionPhotoException when imageUri is blank`() = runTest {
        // Arrange
        val params = mockParams.copy(
            name = "Test Addition",
            isVisible = true,
            newImageUri = ""
        )

        coEvery { dataStoreRepo.getToken() } returns "token"

        // Act & Assert
        assertThrows(AdditionPhotoException::class.java) {
            runBlocking { useCase(params) }
        }
    }

    val mockParams = CreateAdditionUseCase.Params(
        name = "Test Addition",
        isVisible = true,
        price = 100,
        newImageUri = "photoUrl",
        fullName = "Full Test Addition",
        priority = 1,
        tag = "test_tag"
    )
}
