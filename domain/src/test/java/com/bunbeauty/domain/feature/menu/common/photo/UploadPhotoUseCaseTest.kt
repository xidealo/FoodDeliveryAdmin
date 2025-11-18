package com.bunbeauty.domain.feature.menu.common.photo

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UploadPhotoUseCaseTest {
    private val imageUri = "uri"
    private val username = "username"
    private val width = 1000
    private val height = 667

    private val photoRepo: PhotoRepo = mockk()
    private val getUsernameUseCase: GetUsernameUseCase =
        mockk {
            coEvery { this@mockk.invoke() } returns username
        }
    private lateinit var uploadPhotoUseCase: UploadPhotoUseCase

    @BeforeTest
    fun setup() {
        uploadPhotoUseCase =
            UploadPhotoUseCase(
                photoRepo = photoRepo,
                getUsernameUseCase = getUsernameUseCase,
            )
    }

    @Test
    fun `throws MenuProductUploadingImageException when uploading failed`() =
        runTest {
            coEvery {
                photoRepo.uploadPhoto(
                    uri = imageUri,
                    username = username,
                    width = width,
                    height = height,
                )
            } returns null

            assertFailsWith<MenuProductUploadingImageException> {
                uploadPhotoUseCase(imageUri = imageUri)
            }
        }

    @Test
    fun `return photo when photo successfully uploaded`() =
        runTest {
            val photo = Photo.mock.copy(url = "url")
            coEvery {
                photoRepo.uploadPhoto(
                    uri = imageUri,
                    username = username,
                    width = width,
                    height = height,
                )
            } returns photo

            val result = uploadPhotoUseCase(imageUri = imageUri)

            assertEquals(photo, result)
        }
}
