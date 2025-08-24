package com.bunbeauty.domain.feature.additionlist

import android.util.Log
import com.bunbeauty.common.Constants.ADDITION_HEIGHT
import com.bunbeauty.common.Constants.ADDITION_WIDTH
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.feature.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.model.addition.CreateAdditionModel
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo

private const val CREATE_ADDITION_TAG = "CreateAdditionUseCase"

class CreateAdditionUseCase(
    private val additionRepo: AdditionRepo,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val dataStoreRepo: DataStoreRepo

) {

    suspend operator fun invoke(
        createAdditionModel: CreateAdditionModel
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        when {
            createAdditionModel.name.isNullOrBlank() -> throw AdditionNameException()
            createAdditionModel.priority == null -> throw AdditionPriorityException()
        }

        val newPhotoLink: String? = uploadNewPhoto(newImageUri = createAdditionModel.newImageUri)

        removeOldPhotoIfContains(
            photoLink = createAdditionModel.photoLink,
            newImageUri = createAdditionModel.newImageUri
        )

        additionRepo.createAddition(
            token = token,
            createAdditionModel = createAdditionModel.copy(
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
                Log.e(CREATE_ADDITION_TAG, "Photo deletion failed ${it.message}")
            }
        }
    }

}