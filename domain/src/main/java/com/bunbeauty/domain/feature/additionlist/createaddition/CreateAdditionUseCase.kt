package com.bunbeauty.domain.feature.additionlist.createaddition

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.model.addition.CreateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class CreateAdditionUseCase @Inject constructor(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val getUsernameUseCase: GetUsernameUseCase
) {

    data class Params(
        val name: String,
        val priority: Int?,
        val fullName: String?,
        val price: String,
        val isVisible: Boolean,
        val photoLink: String?,
    )

    suspend operator fun invoke(params: Params) {
        val name = params.name.takeIf { name ->
            name.isNotBlank()
        } ?: throw MenuProductNameException()
        val price = params.price.toIntOrNull()
            ?.takeIf { it > 0 }
            ?: throw MenuProductNewPriceException()




        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        additionRepo.post(
            token = token,
            createAddition = CreateAddition(
                name = name,
                price = price,
                priority = params.priority?: 1,
                fullName = params.fullName ?: "",
                isVisible = params.isVisible,
                photoLink = params.photoLink
            )
        )
    }
}