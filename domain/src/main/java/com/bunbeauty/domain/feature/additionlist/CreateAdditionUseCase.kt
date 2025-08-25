package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.common.Constants.ADDITION_HEIGHT
import com.bunbeauty.common.Constants.ADDITION_WIDTH
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.feature.photo.UploadPhotoUseCase
import com.bunbeauty.domain.model.addition.CreateAdditionModel
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class CreateAdditionUseCase(
    private val additionRepo: AdditionRepo,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val dataStoreRepo: DataStoreRepo

) {
    data class Params(
        val name: String,
        val isVisible: Boolean,
        val newImageUri: String,
        val price: Int?,
        val fullName: String?,
        val priority: Int?,
        val tag: String?
    )

    suspend operator fun invoke(
        params: Params
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        when {
            params.name.isBlank() -> throw AdditionNameException()
        }

        val newPhotoLink: String = uploadPhotoUseCase(
            imageUri = params.newImageUri,
            width = ADDITION_WIDTH,
            height = ADDITION_HEIGHT
        ).url

        additionRepo.createAddition(
            token = token,
            createAdditionModel = CreateAdditionModel(
                name = params.name,
                isVisible = params.isVisible,
                price = params.price,
                photoLink = newPhotoLink,
                fullName = params.fullName,
                priority = params.priority,
                tag = params.tag
            )
        )
    }
}
