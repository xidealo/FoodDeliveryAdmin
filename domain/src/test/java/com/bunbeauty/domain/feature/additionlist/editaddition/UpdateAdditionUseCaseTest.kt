package com.bunbeauty.domain.feature.additionlist.editaddition

import android.util.Log
import com.bunbeauty.domain.MockLogRule
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.feature.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.feature.additionlist.UpdateAdditionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UpdateAdditionUseCaseTest {
    private val additionRepo: AdditionRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val uploadPhotoUseCase: UploadPhotoUseCase = mockk()
    private val deletePhotoUseCase: DeletePhotoUseCase = mockk()
    private lateinit var updateAdditionUseCase: UpdateAdditionUseCase

    @get:Rule
    val mockLogRule = MockLogRule()

    @BeforeTest
    fun setup() {
        updateAdditionUseCase = UpdateAdditionUseCase(
            additionRepo = additionRepo,
            dataStoreRepo = dataStoreRepo,
            uploadPhotoUseCase = uploadPhotoUseCase,
            deletePhotoUseCase = deletePhotoUseCase
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
            price = 10,
            photoLink = null,
            newImageUri = null
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            additionRepo.updateAddition(
                updateAddition = updateAddition,
                token = token,
                additionUuid = additionUuidMock
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
    fun `invoke should call UploadPhotoUseCase when newImageUri not null`() = runTest {
        // Given
        val token = "valid_token"
        val additionUuidMock = "uuid"
        val updateAddition = updateAdditionMock.copy(
            name = "Бекон",
            priority = 1,
            price = 10,
            photoLink = "some url",
            newImageUri = "some url"
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { uploadPhotoUseCase("some url", 240, 240) } returns Photo("some url")
        coEvery {
            additionRepo.updateAddition(
                updateAddition = updateAddition,
                token = token,
                additionUuid = additionUuidMock
            )
        } returns Unit
        every { Log.e(any(), any()) } returns 0

        // When
        updateAdditionUseCase.invoke(additionUuidMock, updateAddition)

        // Then
        coVerify { uploadPhotoUseCase("some url", 240, 240) }
    }

    @Test
    fun `invoke should call DeletePhotoUseCase when has photoLink and newImageUri not null`() =
        runTest {
            // Given
            val token = "valid_token"
            val additionUuidMock = "uuid"
            val updateAddition = updateAdditionMock.copy(
                name = "Бекон",
                priority = 1,
                price = 10,
                photoLink = "some url",
                newImageUri = "some url"
            )
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { uploadPhotoUseCase("some url", 240, 240) } returns Photo("some url")
            coEvery {
                additionRepo.updateAddition(
                    updateAddition = updateAddition,
                    token = token,
                    additionUuid = additionUuidMock
                )
            } returns Unit
            every { Log.e(any(), any()) } returns 0

            // When
            updateAdditionUseCase.invoke(additionUuidMock, updateAddition)

            // Then
            coVerify { deletePhotoUseCase("some url") }
        }

    private val updateAdditionMock = UpdateAddition(
        name = "",
        priority = 0,
        fullName = "",
        price = 0,
        photoLink = "",
        isVisible = false,
        newImageUri = ""
    )
}
