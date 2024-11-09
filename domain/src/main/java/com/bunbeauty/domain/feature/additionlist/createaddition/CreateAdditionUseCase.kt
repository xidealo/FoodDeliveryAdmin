package com.bunbeauty.domain.feature.additionlist.createaddition

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additionlist.common.validation.ValidateAdditionNameUseCase
import com.bunbeauty.domain.feature.additionlist.common.validation.ValidateAdditionNewPriceUseCase
import com.bunbeauty.domain.feature.additionlist.common.validation.ValidateAdditionPriorityUseCase
import com.bunbeauty.domain.feature.additionlist.createaddition.exception.AdditionNotCreatedException
import com.bunbeauty.domain.model.addition.CreateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class CreateAdditionUseCase @Inject constructor(
    private val validateCreateAdditionNewPriceUseCase: ValidateAdditionNewPriceUseCase,
    private val validateCreateAdditionNameUseCase: ValidateAdditionNameUseCase,
    private val validateCreateAdditionPriorityUseCase: ValidateAdditionPriorityUseCase,
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo
) {

    data class Params(
        val name: String,
        val priority: String,
        val fullName: String?,
        val price: String,
        val isVisible: Boolean,
        val photoLink: String?
    )

    suspend operator fun invoke(params: Params) {
        val name = validateCreateAdditionNameUseCase(name = params.name)
        val price = validateCreateAdditionNewPriceUseCase(newPrice = params.price)
        val priority = validateCreateAdditionPriorityUseCase(priority = params.priority)

        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        additionRepo.post(
            token = token,
            createAddition = CreateAddition(
                name = name,
                price = price,
                priority = priority,
                fullName = params.fullName ?: "",
                isVisible = params.isVisible,
                photoLink = params.photoLink
            )
        ) ?: throw AdditionNotCreatedException()
    }
}
