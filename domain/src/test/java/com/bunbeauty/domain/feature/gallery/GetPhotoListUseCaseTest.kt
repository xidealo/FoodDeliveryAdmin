package com.bunbeauty.domain.feature.gallery

import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetPhotoListUseCaseTest {

    private val photoRepo: PhotoRepo = mockk()
    private val getUsernameUseCase: GetUsernameUseCase = mockk()
    private lateinit var getPhotoListUseCase: GetPhotoListUseCase

    @BeforeTest
    fun setup() {
        getPhotoListUseCase = GetPhotoListUseCase(
            photoRepo = photoRepo,
            getUsernameUseCase = getUsernameUseCase
        )
    }

    @Test
    fun `invoke returns list of photos successfully`() = runTest {
        // Given
        val username = "TestUser"
        val lowercaseUsername = "testuser"
        val photoList = listOf(
            Photo(url = "http://example.com/photo1.jpg"),
            Photo(url = "http://example.com/photo2.jpg")
        )
        coEvery { getUsernameUseCase() } returns username
        coEvery { photoRepo.getPhotoList(lowercaseUsername) } returns photoList

        // When
        val result = getPhotoListUseCase.invoke()

        // Then
        assertEquals(photoList, result)
        coVerify { getUsernameUseCase() }
        coVerify { photoRepo.getPhotoList(lowercaseUsername) }
    }

    @Test
    fun `invoke handles empty list of photos`() = runTest {
        // Given
        val username = "TestUser"
        val lowercaseUsername = "testuser"
        val photoList = emptyList<Photo>()
        coEvery { getUsernameUseCase() } returns username
        coEvery { photoRepo.getPhotoList(lowercaseUsername) } returns photoList

        // When
        val result = getPhotoListUseCase.invoke()

        // Then
        assertEquals(photoList, result)
        coVerify { getUsernameUseCase() }
        coVerify { photoRepo.getPhotoList(lowercaseUsername) }
    }
}
