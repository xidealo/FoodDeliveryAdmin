package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.feature.photo.DeletePhotoUseCase
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.model.addition.CreateAdditionModel
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo

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

        //   val newPhotoLink: String? = uploadNewPhoto(newImageUri = updateAddition.newImageUri)

//        removeOldPhotoIfContains(
//            photoLink = updateAddition.photoLink,
//            newImageUri = updateAddition.newImageUri
//        )
//
//        additionRepo.updateAddition(
//            additionUuid = additionUuid,
//            token = token,
//            updateAddition = updateAddition.copy(
//                photoLink = newPhotoLink
//            )
//        )
    }

}