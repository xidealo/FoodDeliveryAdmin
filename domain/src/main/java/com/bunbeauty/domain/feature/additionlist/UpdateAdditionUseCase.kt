package com.bunbeauty.domain.feature.additionlist

import android.util.Log
import com.bunbeauty.common.Constants.ADDITION_HEIGHT
import com.bunbeauty.common.Constants.ADDITION_WIDTH
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.feature.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo

private const val UPDATE_ADDITION_TAG = "UpdateAdditionUseCase"

class UpdateAdditionUseCase(
    private val additionRepo: AdditionRepo,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(
        additionUuid: String,
        updateAddition: UpdateAddition,
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        when {
            updateAddition.name.isNullOrBlank() -> throw AdditionNameException()
        }

        val newPhotoLink: String? = uploadNewPhoto(newImageUri = updateAddition.newImageUri)

        removeOldPhotoIfContains(
            photoLink = updateAddition.photoLink,
            newImageUri = updateAddition.newImageUri,
        )

        additionRepo.updateAddition(
            additionUuid = additionUuid,
            token = token,
            updateAddition =
                updateAddition.copy(
                    photoLink = newPhotoLink,
                ),
        )
    }

    private suspend fun uploadNewPhoto(newImageUri: String?): String? {
        if (newImageUri == null) return null

        return uploadPhotoUseCase(
            imageUri = newImageUri,
            width = ADDITION_WIDTH,
            height = ADDITION_HEIGHT,
        ).url
    }

    private suspend fun removeOldPhotoIfContains(
        photoLink: String?,
        newImageUri: String?,
    ) {
        if (photoLink != null && newImageUri != null) {
            runCatching {
                deletePhotoUseCase(photoLink = photoLink)
            }.onFailure {
                Log.e(UPDATE_ADDITION_TAG, "Photo deletion failed ${it.message}")
            }
        }
    }
}
