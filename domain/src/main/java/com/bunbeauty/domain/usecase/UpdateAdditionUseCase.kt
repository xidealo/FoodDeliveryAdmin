package com.bunbeauty.domain.usecase

import android.util.Log
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.feature.menu.common.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.menu.common.photo.UploadPhotoUseCase
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo

private const val UPDATE_ADDITION_TAG = "UpdateAdditionUseCase"
private const val ADDITION_WIDTH = 240
private const val ADDITION_HEIGHT = 240

class UpdateAdditionUseCase(
    private val additionRepo: AdditionRepo,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        additionUuid: String,
        updateAddition: UpdateAddition
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        when {
            updateAddition.name.isNullOrBlank() -> throw AdditionNameException()
            updateAddition.priority == null -> throw AdditionPriorityException()
        }

        val newPhotoLink: String? = uploadNewPhoto(newImageUri = updateAddition.newImageUri)

        removeOldPhotoIfContains(
            photoLink = updateAddition.photoLink,
            newImageUri = updateAddition.newImageUri
        )

        additionRepo.updateAddition(
            additionUuid = additionUuid,
            token = token,
            updateAddition = updateAddition.copy(
                photoLink = newPhotoLink
            )
        )
    }

    private suspend fun uploadNewPhoto(newImageUri: String?): String? {
        if (newImageUri == null) return null

        return uploadPhotoUseCase(
            imageUri = newImageUri,
            width = ADDITION_WIDTH,
            height = ADDITION_HEIGHT
        ).url
    }

    private suspend fun removeOldPhotoIfContains(
        photoLink: String?,
        newImageUri: String?
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
