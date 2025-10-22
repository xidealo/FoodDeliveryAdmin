package com.bunbeauty.domain.feature.photo

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo

private const val DEFAULT_WIDTH = 1000
private const val DEFAULT_HEIGHT = 667

class UploadPhotoUseCase(
    private val photoRepo: PhotoRepo,
    private val getUsernameUseCase: GetUsernameUseCase
) {

    suspend operator fun invoke(
        imageUri: String,
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT
    ): Photo {
        return photoRepo.uploadPhoto(
            uri = imageUri,
            username = getUsernameUseCase(),
            width = width,
            height = height
        ) ?: throw MenuProductUploadingImageException()
    }
}
