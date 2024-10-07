package com.bunbeauty.domain.feature.menu.common.photo

import com.bunbeauty.domain.repo.PhotoRepo
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val photoRepo: PhotoRepo
) {

    suspend operator fun invoke(photoLink: String) {
        photoRepo.deletePhoto(photoLink)
    }
}
