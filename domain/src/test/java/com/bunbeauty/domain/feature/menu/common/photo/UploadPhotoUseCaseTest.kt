package com.bunbeauty.domain.feature.menu.common.photo

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UploadPhotoUseCaseTest {

    private val imageUri = "uri"
    private val fileSize = 200L
    private val quality = 50
    private val username = "username"

    private val photoRepo: PhotoRepo = mockk {
        every { getFileSizeInMb(uri = imageUri) } returns fileSize
    }
    private val calculateImageCompressQualityUseCase: CalculateImageCompressQualityUseCase = mockk {
        every { this@mockk.invoke(fileSize) } returns quality
    }
    private val getUsernameUseCase: GetUsernameUseCase = mockk {
        coEvery { this@mockk.invoke() } returns username
    }
    private lateinit var uploadPhotoUseCase: UploadPhotoUseCase

    @BeforeTest
    fun setup() {
        uploadPhotoUseCase = UploadPhotoUseCase(
            photoRepo = photoRepo,
            calculateImageCompressQualityUseCase = calculateImageCompressQualityUseCase,
            getUsernameUseCase = getUsernameUseCase,
        )
    }

    @Test
    fun `throws MenuProductImageException when uri is null`() = runTest {
        assertFailsWith<MenuProductImageException> {
            uploadPhotoUseCase(imageUri = null)
        }
    }

    @Test
    fun `throws MenuProductUploadingImageException when uploading failed`() = runTest {
        coEvery {
            photoRepo.uploadPhoto(
                uri = imageUri,
                compressQuality = quality,
                username = username,
            )
        } returns null

        assertFailsWith<MenuProductUploadingImageException> {
            uploadPhotoUseCase(imageUri = imageUri)
        }
    }

    @Test
    fun `return photo when photo successfully uploaded`() = runTest {
        val photo = Photo(url = "url")
        coEvery {
            photoRepo.uploadPhoto(
                uri = imageUri,
                compressQuality = quality,
                username = username,
            )
        } returns photo

        val result = uploadPhotoUseCase(imageUri = imageUri)

        assertEquals(photo, result)
    }

}