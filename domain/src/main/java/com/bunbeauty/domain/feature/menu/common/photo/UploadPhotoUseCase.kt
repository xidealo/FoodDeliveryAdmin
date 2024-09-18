package com.bunbeauty.domain.feature.menu.common.photo

import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import javax.inject.Inject

class UploadPhotoUseCase @Inject constructor(
    private val photoRepo: PhotoRepo,
    private val getUsernameUseCase: GetUsernameUseCase
) {

    suspend operator fun invoke(imageUri: String): Photo {
        return photoRepo.uploadPhoto(
            uri = imageUri,
            username = getUsernameUseCase()
        ) ?: throw MenuProductUploadingImageException()
    }
}
