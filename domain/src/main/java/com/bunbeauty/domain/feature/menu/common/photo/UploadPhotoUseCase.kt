package com.bunbeauty.domain.feature.menu.common.photo

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import javax.inject.Inject

class UploadPhotoUseCase @Inject constructor(
    private val photoRepo: PhotoRepo,
    private val calculateImageCompressQualityUseCase: CalculateImageCompressQualityUseCase,
    private val getUsernameUseCase: GetUsernameUseCase
) {

    suspend operator fun invoke(imageUri: String?): Photo {
        imageUri ?: throw MenuProductImageException()

        val fileSize = photoRepo.getFileSizeInMb(uri = imageUri)
        val compressQuality = calculateImageCompressQualityUseCase(fileSize = fileSize)

        return photoRepo.uploadPhoto(
            uri = imageUri,
            compressQuality = compressQuality,
            username = getUsernameUseCase()
        ) ?: throw MenuProductUploadingImageException()
    }
}
