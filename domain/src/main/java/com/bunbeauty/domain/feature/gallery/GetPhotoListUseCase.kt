package com.bunbeauty.domain.feature.gallery

import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(
    private val photoRepo: PhotoRepo,
    private val getUsernameUseCase: GetUsernameUseCase
) {
    suspend operator fun invoke(): List<Photo> {
        return photoRepo.getPhotoList(username = getUsernameUseCase().lowercase())
    }
}
